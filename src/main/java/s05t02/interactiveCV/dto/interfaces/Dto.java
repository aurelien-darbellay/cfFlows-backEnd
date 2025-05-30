package s05t02.interactiveCV.dto.interfaces;


public interface Dto {
    <T extends MapableToDto> T mapDtoToBackEndObject();
}

