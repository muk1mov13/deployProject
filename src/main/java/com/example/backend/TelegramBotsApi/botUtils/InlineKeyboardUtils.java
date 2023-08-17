package com.example.backend.TelegramBotsApi.botUtils;

import com.example.backend.entity.CustomerCategory;
import com.example.backend.entity.Territory;
import org.springframework.data.domain.Page;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardUtils {
    public static <T> InlineKeyboardMarkup createInlineKeyboard(Page<T> pages, String callbackPrefix) {
        List<T> items = pages.getContent();
        System.out.println(items);
        int currentPage = pages.getPageable().getPageNumber();
        int currentSize = pages.getPageable().getPageSize();
        int totalPages = pages.getTotalPages();
        int endPage = totalPages - 1;
        System.out.println(currentPage);
        System.out.println(totalPages);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        int buttonCount = 0;
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            InlineKeyboardButton button = new InlineKeyboardButton();

            if (item instanceof CustomerCategory) {
                CustomerCategory category = (CustomerCategory) item;
                button.setText(category.getName());
                button.setCallbackData(callbackPrefix + ":" + category.getName());
            } else if (item instanceof Territory) {
                Territory territory = (Territory) item;
                button.setText(territory.getName());
                button.setCallbackData(callbackPrefix + ":" + territory.getName());
            }

            row.add(button);
            buttonCount++;

            if (buttonCount == 3) {
                keyboard.add(row);
                row = new ArrayList<>();
                buttonCount = 0;
            }
        }

// Add the last row if it contains buttons
        if (!row.isEmpty()) {
            keyboard.add(row);
        }
//
        // Add pagination buttons
        List<InlineKeyboardButton> paginationRow = new ArrayList<>();
        if (currentPage > 0) {
            InlineKeyboardButton previous = new InlineKeyboardButton("⬅\uFE0F previous");
            previous.setCallbackData("previous:" + (currentPage - 1) + ":" + callbackPrefix);
            paginationRow.add(previous);
        }
        if (currentPage != endPage && !items.isEmpty()) {
            InlineKeyboardButton next = new InlineKeyboardButton("next ➡\uFE0F");
            next.setCallbackData("next:" + (currentPage + 1) + ":" + callbackPrefix);
            paginationRow.add(next);
        }
        if (!paginationRow.isEmpty()) {
            keyboard.add(paginationRow);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboard);
        return inlineKeyboardMarkup;
    }
}
