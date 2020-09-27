package com.pearl.parsers.json;

import com.google.gson.Gson;
import com.pearl.ui.models.BaseResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class AwaitingApprovalList.
 */
public class AwaitingApprovalList {
    
    /** The gson. */
    Gson gson = new Gson();

    /**
     * Approval list parser.
     *
     * @param baseRasponse the base rasponse
     * @return the string
     */
    public String approvalListParser(String baseRasponse) {
	BaseResponse br = null;
	br = gson.fromJson(baseRasponse, BaseResponse.class);

	return br.getData();

    }

}
