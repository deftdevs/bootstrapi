package com.deftdevs.bootstrapi.commons.util;

import com.deftdevs.bootstrapi.commons.model.type.SerializableFunction;
import com.deftdevs.bootstrapi.commons.model.type.SubEntityOf;

import javax.xml.bind.annotation.XmlElement;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Derives serialized field names from the model classes themselves, so
 * hierarchies expressed by the models (a field of a given type, a
 * {@link SubEntityOf} declaration) never have to be restated as string
 * constants. The status-map keys of the _all endpoint are built from these
 * names and therefore always match the JSON structure of the request.
 * <p>
 * A field matches when its (type-variable-resolved) declared type is the
 * requested type, or when it is a {@link Map} whose value type (or a
 * {@link Collection} whose element type) is the requested type. Inherited
 * fields are included. Every lookup fails fast when no field or more than
 * one field matches, so any ambiguity introduced by a future model change is
 * caught by the unit tests of the code deriving names from it.
 */
public final class FieldNames {

    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    private FieldNames() {
    }

    /**
     * Returns the serialized name of the field holding values of
     * {@code subEntityClass} within its parent model, which the sub-model
     * itself declares via {@link SubEntityOf}
     * (e.g. {@code MailServerPopModel} → {@code pop}).
     */
    public static String of(final Class<?> subEntityClass) {
        final SubEntityOf subEntityOf = subEntityClass.getAnnotation(SubEntityOf.class);
        if (subEntityOf == null) {
            throw new IllegalArgumentException(
                    subEntityClass.getName() + " must be annotated with @SubEntityOf");
        }
        return of(subEntityOf.value(), subEntityClass);
    }

    /**
     * Returns the serialized name of the (unique) field of {@code modelClass}
     * (including inherited fields) that holds values of {@code fieldType},
     * honoring an explicit {@code @XmlElement(name = ...)} override.
     */
    public static String of(final Class<?> modelClass, final Class<?> fieldType) {
        return CACHE.computeIfAbsent(modelClass.getName() + "#" + fieldType.getName(),
                key -> resolve(modelClass, fieldType));
    }

    /**
     * Returns the full slash-separated field path from {@code rootClass} to
     * the (unique) field holding values of {@code leafType}, walking the
     * model hierarchy (e.g. a product {@code _AllModel} +
     * {@code SettingsGeneralModel} → {@code settings/general}, +
     * {@code MailServerModel} → {@code mailServer}).
     */
    /**
     * Returns the serialized name of the field read by the given getter
     * reference (e.g. {@code _AllModel::getTrustedProxies} →
     * {@code trustedProxies}), honoring an explicit
     * {@code @XmlElement(name = ...)} override. Use this for fields whose
     * type is not a model class (e.g. a list of strings), where a type-based
     * lookup would couple the name to "the only field of that type" and
     * break as soon as another field of the same type is added.
     */
    public static <T, R> String of(final SerializableFunction<T, R> getter) {
        final SerializedLambda lambda = serializedLambda(getter);
        final String className = lambda.getImplClass().replace('/', '.');
        return CACHE.computeIfAbsent(className + "::" + lambda.getImplMethodName(),
                key -> resolveGetter(getter, lambda, className));
    }

    private static String resolveGetter(final SerializableFunction<?, ?> getter,
            final SerializedLambda lambda, final String className) {

        final String methodName = lambda.getImplMethodName();
        final String fieldName;
        if (methodName.startsWith("get") && methodName.length() > 3) {
            fieldName = decapitalize(methodName.substring(3));
        } else if (methodName.startsWith("is") && methodName.length() > 2) {
            fieldName = decapitalize(methodName.substring(2));
        } else {
            throw new IllegalArgumentException(
                    "Not a getter reference: " + className + "::" + methodName);
        }

        final Class<?> modelClass;
        try {
            modelClass = Class.forName(className, false, getter.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot load the getter's declaring class", e);
        }

        for (final Field field : declaredAndInheritedFields(modelClass)) {
            if (field.getName().equals(fieldName)) {
                return serializedName(field);
            }
        }
        throw new IllegalArgumentException(String.format(
                "%s has no field backing %s()", className, methodName));
    }

    /**
     * A getter reference assigned to a {@link SerializableFunction} carries
     * its method metadata in the {@link SerializedLambda} returned by the
     * compiler-generated {@code writeReplace} method.
     */
    private static SerializedLambda serializedLambda(final SerializableFunction<?, ?> getter) {
        try {
            final Method writeReplace = getter.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (SerializedLambda) writeReplace.invoke(getter);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot introspect the getter reference", e);
        }
    }

    private static String decapitalize(final String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    public static String pathOf(final Class<?> rootClass, final Class<?> leafType) {
        return CACHE.computeIfAbsent(rootClass.getName() + "->" + leafType.getName(),
                key -> resolvePath(rootClass, leafType));
    }

    private static String resolvePath(final Class<?> rootClass, final Class<?> leafType) {
        final java.util.List<String> paths = candidatePaths(rootClass, leafType, new java.util.HashSet<>());
        if (paths.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                    "%s has no field path to %s", rootClass.getName(), leafType.getName()));
        }
        if (paths.size() > 1) {
            throw new IllegalArgumentException(String.format(
                    "%s has more than one field path to %s: %s",
                    rootClass.getName(), leafType.getName(), String.join(", ", paths)));
        }
        return paths.get(0);
    }

    private static java.util.List<String> candidatePaths(final Class<?> rootClass, final Class<?> leafType,
            final java.util.Set<Class<?>> visited) {

        final java.util.List<String> paths = new java.util.ArrayList<>();
        if (!visited.add(rootClass)) {
            return paths;
        }

        final Map<TypeVariable<?>, Type> bindings = typeVariableBindings(rootClass);
        for (final Field field : declaredAndInheritedFields(rootClass)) {
            if (matches(field, leafType, bindings)) {
                paths.add(serializedName(field));
                continue;
            }

            final Type resolvedType = resolveType(field.getGenericType(), bindings);
            if (resolvedType instanceof Class
                    && isModel((Class<?>) resolvedType)
                    && !((Class<?>) resolvedType).isEnum()) {
                for (final String subPath : candidatePaths((Class<?>) resolvedType, leafType, visited)) {
                    paths.add(serializedName(field) + "/" + subPath);
                }
            }
        }

        visited.remove(rootClass);
        return paths;
    }

    private static String resolve(final Class<?> modelClass, final Class<?> fieldType) {
        Field match = null;

        for (final Field field : declaredAndInheritedFields(modelClass)) {
            if (!matches(field, fieldType, typeVariableBindings(modelClass))) {
                continue;
            }
            if (match != null) {
                throw new IllegalArgumentException(String.format(
                        "%s has more than one field holding %s: %s, %s",
                        modelClass.getName(), fieldType.getName(), match.getName(), field.getName()));
            }
            match = field;
        }

        if (match == null) {
            throw new IllegalArgumentException(String.format(
                    "%s has no field holding %s", modelClass.getName(), fieldType.getName()));
        }
        return serializedName(match);
    }

    private static java.util.List<Field> declaredAndInheritedFields(final Class<?> modelClass) {
        final java.util.List<Field> fields = new java.util.ArrayList<>();
        for (Class<?> current = modelClass; current != null && current != Object.class;
                current = current.getSuperclass()) {
            for (final Field field : current.getDeclaredFields()) {
                if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    /** Maps the type variables of superclasses to the concrete types bound by {@code modelClass}. */
    private static Map<TypeVariable<?>, Type> typeVariableBindings(final Class<?> modelClass) {
        final Map<TypeVariable<?>, Type> bindings = new HashMap<>();
        for (Class<?> current = modelClass; current != null && current != Object.class;
                current = current.getSuperclass()) {
            final Type genericSuperclass = current.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                final ParameterizedType parameterized = (ParameterizedType) genericSuperclass;
                final TypeVariable<?>[] variables = current.getSuperclass().getTypeParameters();
                final Type[] arguments = parameterized.getActualTypeArguments();
                for (int i = 0; i < variables.length; i++) {
                    bindings.put(variables[i], resolveType(arguments[i], bindings));
                }
            }
        }
        return bindings;
    }

    private static Type resolveType(final Type type, final Map<TypeVariable<?>, Type> bindings) {
        if (type instanceof TypeVariable) {
            return bindings.getOrDefault(type, type);
        }
        return type;
    }

    private static boolean matches(final Field field, final Class<?> fieldType,
            final Map<TypeVariable<?>, Type> bindings) {

        final Type resolvedType = resolveType(field.getGenericType(), bindings);
        if (resolvedType.equals(fieldType)) {
            return true;
        }

        if (!(resolvedType instanceof ParameterizedType)) {
            return false;
        }

        final ParameterizedType parameterized = (ParameterizedType) resolvedType;
        final Type[] typeArguments = parameterized.getActualTypeArguments();
        if (Map.class.isAssignableFrom(field.getType()) && typeArguments.length == 2) {
            return resolveType(typeArguments[1], bindings).equals(fieldType);
        }
        if (Collection.class.isAssignableFrom(field.getType()) && typeArguments.length == 1) {
            return resolveType(typeArguments[0], bindings).equals(fieldType);
        }
        return false;
    }

    private static String serializedName(final Field field) {
        final XmlElement xmlElement = field.getAnnotation(XmlElement.class);
        if (xmlElement != null && !"##default".equals(xmlElement.name())) {
            return xmlElement.name();
        }
        return field.getName();
    }

    private static boolean isModel(final Class<?> type) {
        return type.getPackage() != null
                && type.getPackage().getName().startsWith("com.deftdevs.bootstrapi");
    }

}
