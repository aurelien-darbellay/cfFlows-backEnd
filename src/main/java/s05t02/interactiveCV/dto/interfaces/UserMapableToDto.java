package s05t02.interactiveCV.dto.interfaces;

import s05t02.interactiveCV.dto.DashBoardDto;
import s05t02.interactiveCV.dto.DocFacade;
import s05t02.interactiveCV.exception.IllegalInterfaceImplementation;
import s05t02.interactiveCV.model.User;

import java.util.List;

public interface UserMapableToDto extends MapableToDto {
    @SuppressWarnings("unchecked")
    @Override
    default DashBoardDto mapToDto() {
        if (!(this instanceof User user)) throw new IllegalInterfaceImplementation(UserMapableToDto.class,User.class);
        List<DocFacade> documentsIds = user.getInteractiveDocuments().stream().map(doc -> DocFacade.of(doc.getId(), doc.getTitle())).toList();
        return new DashBoardDto(user.getUsername(), user.getFirstname(), user.getLastname(), documentsIds);
    }
}
