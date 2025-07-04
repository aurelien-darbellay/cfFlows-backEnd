package s05t02.interactiveCV.dto;

import java.util.List;

public record PublicViewsDashboardDto(String username, List<PublicViewFacade> publicViews) {
    static public PublicViewsDashboardDto of(String username, List<PublicViewFacade> publicViews) {
        return new PublicViewsDashboardDto(username, publicViews);
    }
}
