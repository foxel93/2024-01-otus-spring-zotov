package ru.otus.hw.utils;

import java.util.ArrayList;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@UtilityClass
public class UtilsController {
    public static PageRequest pageRequest(int pageNumber, int pageSize, String[] sorts) {
        return PageRequest.of(pageNumber, pageSize, sort(sorts));
    }

    public static Sort sort(String[] sorts) {
        var orders = new ArrayList<Sort.Order>();

        if (sorts[0].contains(",")) {
            for (var sortOrder : sorts) {
                var sortPair = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(sortPair[1]), sortPair[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortDirection(sorts[1]), sorts[0]));
        }
        return Sort.by(orders);
    }

    private static Sort.Direction getSortDirection(String sortDirection) {
        return sortDirection.contains("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }
}
