package com.mwaisaka.Library.Management.System.enums;

public enum BookGenre {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    SCIENCE_FICTION("Science Fiction"),
    MYSTERY("Mystery"),
    BIOGRAPHY("Biography"),
    HISTORY("History"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology");

    private final String displayName;

    BookGenre(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
