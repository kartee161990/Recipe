package com.myorg.recipe.search;

public enum SearchOperation {

    CONTAINS, DOES_NOT_CONTAIN, EQUAL, NOT_EQUAL, WITH,
    ANY, ALL;

    public static SearchOperation getOperation(String input) {
        return switch (input) {
            case "cn" -> CONTAINS;
            case "nc" -> DOES_NOT_CONTAIN;
            case "eq" -> EQUAL;
            case "ne" -> NOT_EQUAL;
            case "with" -> WITH;
            default -> null;
        };
    }

    public static SearchOperation getDataOption(String dataOption){
        return switch (dataOption) {
            case "all" -> ALL;
            case "any" -> ANY;
            default -> null;
        };
    }

}
