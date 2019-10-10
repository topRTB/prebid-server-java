package org.prebid.server.proto.openrtb.ext.request.toprtb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Defines the contract for bidRequest.imp[i].ext.toprtb
 */
@AllArgsConstructor(
        staticName = "of"
)
@Value
public class ExtImpToprtb {
    @JsonProperty("adUnitId")
    String adUnitId;
}
