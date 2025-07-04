package s05t02.interactiveCV.dto.interfaces;


import s05t02.interactiveCV.dto.PublicViewFacade;
import s05t02.interactiveCV.exception.IllegalInterfaceImplementation;
import s05t02.interactiveCV.model.publicViews.PublicView;

@SuppressWarnings("unchecked")
public interface PublicViewMapableToDto extends MapableToDto {
    @Override
    default PublicViewFacade mapToDto() {
        if (!(this instanceof PublicView publicView))
            throw new IllegalInterfaceImplementation(PublicViewMapableToDto.class, PublicView.class);
        return new PublicViewFacade(((PublicView) this).getId(), ((PublicView) this).getDocument().getTitle());
    }
}
