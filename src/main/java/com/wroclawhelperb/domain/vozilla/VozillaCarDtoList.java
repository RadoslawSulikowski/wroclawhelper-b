package com.wroclawhelperb.domain.vozilla;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VozillaCarDtoList {

    @JsonProperty("objects")
    private List<VozillaCarDto> cars;

    public List<VozillaCarDto> getCars() {
        return null;
    }
}
