package com.buzzlers.jhelpdesk.web.utils;

public class PagingTools {

    public static int calculatePages(int itemsCount, int pageSize) {
        return itemsCount > pageSize
                ? itemsCount / pageSize + (itemsCount % pageSize == 0 ? 0 : 1)
                : 1;
    }

    public static int calculateOffset(int pageSize, int page) {
        return pageSize * (page-1);
    }
}
