package s05t02.interactiveCV.dto;

import s05t02.interactiveCV.dto.interfaces.Dto;
import s05t02.interactiveCV.dto.interfaces.MapableToDto;

import java.util.List;


public record DashBoardDto(String username, String firstname, String lastname, String role, List<DocFacade> documentsIds) implements Dto {
    @Override
    public <T extends MapableToDto> T mapDtoToBackEndObject() {
        return null;
    }
}
