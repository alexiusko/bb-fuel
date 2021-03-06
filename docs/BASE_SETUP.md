# Base setup

## Access control setup
- Root legal entity with user external id from property `root.entitlements.admin` as entitlements admin
- Legal entities (under the root legal entity `C000000`) per legal entity entry with users array in the file [legal-entities-with-users.json](../src/main/resources/data/legal-entities-with-users.json) - configurable, see section *Custom data*

For legal entities and users in the file [legal-entities-with-users.json](../src/main/resources/data/legal-entities-with-users.json):
- All job profiles are assigned to the users via master service agreement of the legal entities from the input file.
    - Job profiles are defined in [job-profiles.json](../src/main/resources/data/job-profiles.json)
    - You can limit the job profiles assigned to a user by explicitly referring to one of the roles defined in the job profile. E.g. the `retail` role on a user will result in only assignment of the job profile `Retail User`.   
- All product groups are assigned to the users via master service agreement of the legal entities from the input file.
    - Product groups are defined in [product-group-seed.json](../src/main/resources/data/product-group-seed.json)
    - You can limit product groups assigned to a user by explicitly referring to the actual names in productGroupNames of a user. E.g. a user with only one productGroupName `Payroll` will only be granted permissions for the `Payroll` product group.

## Product summary setup
- Default products: [products.json](../src/main/resources/data/products.json)
- Arrangements in product groups are defined in [product-group-seed.json](../src/main/resources/data/product-group-seed.json)
    - Non current accounts (productId not equal to `1`) will be 10% of total amount of arrangements, only in case of productIds also contain `1`, otherwise the total amount will be used for the non current accounts.
- In case of current account arrangements random debit cards (configurable) are associated
- Additionally (by default disabled) possible to ingest balance history based on a weekly balance history items for the past quarter, plus daily balance history items for the past week.
    - Only works if property `ingest.access.control` is set to `true` due to the required external arrangement id when ingesting balance history items.
    - This external arrangement id is only available when creating an arrangement (part of the access control setup). The external arrangement id is currently not retrievable via any REST endpoint.

## Service agreements setup
Default service agreements (each object represents one service agreement): [service-agreements.json](../src/main/resources/data/service-agreements.json)
- Legal entity ids will be retrieved via the external user ids given in the json file to set up the service agreements.
- Same access control setup for function/data groups and permissions for each service agreement, taking into account:
- For each participant that shares accounts, arrangements are ingested under its legal entity
- Job profiles/product groups will be ingested under each service agreement
- Each participant that shares users are assigned permissions

## Users setup
By default only the following users are covered:
- Users with permissions as described under *Access control setup* and *Product summary setup*: [legal-entities-with-users.json](../src/main/resources/data/legal-entities-with-users.json)

If more/other users are required, you can provide your own `json` files, see [Custom data](CUSTOM_DATA.md).
