package s05t02.interactiveCV.dto.interfaces;

import s05t02.interactiveCV.dto.CvToDto;
import s05t02.interactiveCV.exception.IllegalInterfaceImplementation;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;

public interface CvMapableToDto extends MapableToDto {
    @SuppressWarnings("unchecked")
    @Override
    default CvToDto mapToDto(){
        if(!(this instanceof InteractiveCv)) throw new IllegalInterfaceImplementation(CvMapableToDto.class, InteractiveCv.class);
        return (CvToDto) this;
    }
}
