package org.prebid.server.bidder.toprtb;

import org.prebid.server.bidder.OpenrtbBidder;
import org.prebid.server.proto.openrtb.ext.request.toprtb.ExtImpToprtb;

public class ToprtbBidder extends OpenrtbBidder<ExtImpToprtb> {
    public ToprtbBidder(String endpointUrl) {
        super(endpointUrl, RequestCreationStrategy.SINGLE_REQUEST, ExtImpToprtb.class);
    }
}
