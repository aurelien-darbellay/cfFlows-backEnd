package s05t02.interactiveCV.dto.interfaces;

public interface MapableToDto {
    <T extends Dto> T mapToDto();
}
