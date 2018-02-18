package com.buzzlers.jhelpdesk.web.utils;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.util.ParamEncoder;
import static org.displaytag.tags.TableTagParameters.PARAMETER_PAGE;
import static org.displaytag.tags.TableTagParameters.PARAMETER_SORT;
import static org.displaytag.tags.TableTagParameters.PARAMETER_ORDER;

public class PagingParamsEncoder {

    private final static int DEFAULT_OFFSET = 0;
    private HttpServletRequest request;
    private ParamEncoder paramEncoder;
    private String columnToSort;
    private int pageSize;

    public PagingParamsEncoder(String tableName, String defaultColumnToSort,
                               HttpServletRequest request, int pageSize) {
        this.columnToSort = defaultColumnToSort;
        this.request = request;
        this.pageSize = pageSize;
        this.paramEncoder = new ParamEncoder(tableName);
    }

    public int getOffset() {
        int offset = DEFAULT_OFFSET;
        String page = this.paramEncoder.encodeParameterName(PARAMETER_PAGE);
        if ((request.getParameter(page) != null)
                && (request.getParameter(page).length() > 0)) {
            offset = (Integer.parseInt(request.getParameter(page)) - 1) * pageSize;
        }
        return offset;
    }

    public String getColumnToSort() {
        String myCompanyColumnSorting = this.paramEncoder.encodeParameterName(PARAMETER_SORT);
        if ((request.getParameter(myCompanyColumnSorting) != null)
                && (request.getParameter(myCompanyColumnSorting).length() > 0)) {
            columnToSort = request.getParameter(myCompanyColumnSorting);
        }
        return columnToSort;
    }

    public boolean getOrder() {
        String mySortOrder = null;
        String myCompanySortOrder = this.paramEncoder.encodeParameterName(PARAMETER_ORDER);
        if ((request.getParameter(myCompanySortOrder) != null)
                && (request.getParameter(myCompanySortOrder).length() > 0)) {
            mySortOrder = request.getParameter(myCompanySortOrder);
        }
        return (mySortOrder != null) && mySortOrder.equalsIgnoreCase("1");
    }
}
