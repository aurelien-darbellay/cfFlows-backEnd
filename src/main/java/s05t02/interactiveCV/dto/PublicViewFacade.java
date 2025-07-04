package s05t02.interactiveCV.dto;

import s05t02.interactiveCV.dto.interfaces.Dto;
import s05t02.interactiveCV.dto.interfaces.MapableToDto;

public record PublicViewFacade(String pvId, String docTitle) implements Dto {
    @Override
    public <T extends MapableToDto> T mapDtoToBackEndObject() {
        return null;
    }
}
