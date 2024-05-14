package org.palpalmans.ollive_back.domain.member.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@ToString
@Getter
@Setter
public class GoogleResponse{

    private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    public String getProvider() {

        return "google";
    }

    public String getProviderId() {

        return attribute.get("sub").toString();
    }

    public String getEmail() {

        return attribute.get("email").toString();
    }

    public String getName() {

        return attribute.get("name").toString();
    }

    public String getPicture() {
        return attribute.get("picture").toString();
    }
}
