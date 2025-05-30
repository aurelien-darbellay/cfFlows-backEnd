package s05t02.interactiveCV.dto;

import s05t02.interactiveCV.model.User;

import java.util.List;


public record DashBoardDto(String username, String firstname, String lastname, List<DocFacade> documentsIds) {
    static public DashBoardDto mapUserToDashBoard(User user) {
        List<DocFacade> documentsIds = user.getInteractiveDocuments().stream().map(doc -> DocFacade.of(doc.getId(), doc.getTitle())).toList();
        return new DashBoardDto(user.getUsername(), user.getFirstname(), user.getLastname(), documentsIds);
    }
}
