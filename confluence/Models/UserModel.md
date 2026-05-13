# UserModel
## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
| **username** | **String** |  | [optional] [default to null] |
| **firstName** | **String** |  | [optional] [default to null] |
| **lastName** | **String** |  | [optional] [default to null] |
| **fullName** | **String** |  | [optional] [default to null] |
| **email** | **String** |  | [optional] [default to null] |
| **active** | **Boolean** |  | [optional] [default to null] |
| **password** | **String** |  | [optional] [default to null] |
| **groups** | **Map** | Group memberships keyed by group name. true ensures the user is a member, false ensures the user is not a member, null is a no-op (leaves the membership untouched). Group lifecycle is managed via the top-level groups map; referenced groups must already exist. | [optional] [default to null] |

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)

