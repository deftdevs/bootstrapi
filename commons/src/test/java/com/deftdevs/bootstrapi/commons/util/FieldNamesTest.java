package com.deftdevs.bootstrapi.commons.util;

import org.junit.jupiter.api.Test;

import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldNamesTest {

    private static class LeafModel {
    }

    private static class OtherLeafModel {
    }

    private static class GroupModel {
        private LeafModel leaf;
        private Map<String, OtherLeafModel> others;
    }

    private static class ThirdLeafModel {
    }

    private static class RootModel {
        private GroupModel group;
        private Map<String, LeafModel> directLeaves;
        private List<String> names;

        @XmlElement(name = "renamed")
        private ThirdLeafModel other;

        public List<String> getNames() {
            return names;
        }

        public ThirdLeafModel getOther() {
            return other;
        }

        public RootModel getSelf() {
            return this;
        }
    }

    private static class AmbiguousModel {
        private LeafModel first;
        private LeafModel second;
    }

    @Test
    void ofResolvesDirectField() {
        assertEquals("leaf", FieldNames.of(GroupModel.class, LeafModel.class));
    }

    @Test
    void ofResolvesMapValueField() {
        assertEquals("others", FieldNames.of(GroupModel.class, OtherLeafModel.class));
    }

    @Test
    void ofResolvesCollectionElementField() {
        assertEquals("names", FieldNames.of(RootModel.class, String.class));
    }

    @Test
    void ofHonorsXmlElementName() {
        assertEquals("renamed", FieldNames.of(RootModel.class, ThirdLeafModel.class));
    }

    @Test
    void ofRejectsAmbiguousFields() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldNames.of(AmbiguousModel.class, LeafModel.class));
    }

    @Test
    void ofRejectsUnknownFieldType() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldNames.of(GroupModel.class, RootModel.class));
    }

    @Test
    void ofGetterResolvesTheBackingField() {
        assertEquals("names", FieldNames.of(RootModel::getNames));
    }

    @Test
    void ofGetterHonorsXmlElementName() {
        assertEquals("renamed", FieldNames.of(RootModel::getOther));
    }

    @Test
    void ofGetterRejectsMethodsWithoutBackingField() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldNames.of(RootModel::getSelf));
    }

    @Test
    void pathOfResolvesTopLevelField() {
        assertEquals("group", FieldNames.pathOf(RootModel.class, GroupModel.class));
    }

    @Test
    void pathOfWalksIntoNestedModels() {
        assertEquals("group/others", FieldNames.pathOf(RootModel.class, OtherLeafModel.class));
    }

    @Test
    void pathOfRejectsAmbiguousPaths() {
        // LeafModel is reachable both as root.directLeaves and root.group.leaf
        assertThrows(IllegalArgumentException.class,
                () -> FieldNames.pathOf(RootModel.class, LeafModel.class));
    }
}
