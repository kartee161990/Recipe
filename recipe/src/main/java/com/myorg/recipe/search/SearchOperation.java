package com.myorg.recipe.search;

public enum SearchOperation {

    CONTAINS, DOES_NOT_CONTAIN, EQUAL, NOT_EQUAL, WITH,
    ANY, ALL;

    public static SearchOperation getOperation(String input) {
        switch (input) {
            case "cn": return CONTAINS;
            case "nc": return DOES_NOT_CONTAIN;
            case "eq": return EQUAL;
            case "ne": return NOT_EQUAL;
            case "with": return WITH;

            default: return null;
        }
    }

    public static SearchOperation getDataOption(String dataOption){
        switch(dataOption) {
            case "all": return ALL;
            case "any": return ANY;
            default: return null;
        }
    }

}
