package s05t02.interactiveCV.dto;

import s05t02.interactiveCV.dto.interfaces.Dto;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;

public class CvToDto extends InteractiveCv implements Dto {
    @SuppressWarnings("unchecked")
    @Override
    public InteractiveCv mapDtoToBackEndObject() {
        return this;
    }
}
