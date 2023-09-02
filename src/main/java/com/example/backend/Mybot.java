package com.example.backend;

import com.example.backend.TelegramBotsApi.botUtils.InlineKeyboardUtils;
import com.example.backend.TelegramBotsApi.constants.STATUS;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Mybot extends TelegramLongPollingBot {


    private final TelegramUserRepository telegramUserRepository;
    private final AgentRepository agentRepository;
//    private final CustomerCategoryRepository customerCategoryRepository;
//    private final TerritoryRepository territoryRepository;
//    private final ClientRepository clientRepository;


//    private final String newClient = "➕ Yangi Mijoz qo'shish";
//    private final String clientData = "\uD83D\uDCDD Mijozlar ma’lumotlari";
//    private final String updateClient = "✏\uFE0F Mijoz taxrirlash";
//    private final String searchClient = "\uD83D\uDD0D Mijozni qidirish";

//    private int defaultSize = 3;
//    private int defaultPage = 0;
//    private int defaultPageTerritory = 0;

//    private int addnewClientBtnMessageId;
//    private int updateCustomerCategoryBtnsId;
//    private int updateTerritoryBtnsId;
//    private int deleteDialogPhoneBtnsId;
//    private int deleteDialogINNBtnsId;
//    private int deleteDialogLocationBtnsId;

    //    private String phone;
//    private String Inn;
//    private Double longitude;
//    private Double latitude;
//    private Location location;
    private TelegramUser hasUser = null;

    private Agent hasAgent = null;
    private static final Logger logger = LoggerFactory.getLogger(Mybot.class);

    private String username = "@example_dict_bot";
    private String TOKEN = "5929282413:AAHG3EezjE3iKCGLkp1MpmejzBXugAp9eAY";


//    @SneakyThrows
//    @Autowired
//    public Mybot(TelegramBotsApi api, TelegramUserRepository telegramUserRepository, CustomerCategoryRepository customerCategoryRepository, TerritoryRepository territoryRepository, ClientRepository clientRepository) {
//        this.telegramUserRepository = telegramUserRepository;
//        this.customerCategoryRepository = customerCategoryRepository;
//        this.territoryRepository = territoryRepository;
//        this.clientRepository = clientRepository;
//        api.registerBot(this);
//    }

    @SneakyThrows
    @Autowired
    public Mybot(TelegramBotsApi api, TelegramUserRepository telegramUserRepository, AgentRepository agentRepository) {
        this.telegramUserRepository = telegramUserRepository;
        this.agentRepository = agentRepository;
        api.registerBot(this);
    }

    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, TOKEN);
    }

    //    @Override
//    @SneakyThrows
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage()) {
//            Message message = update.getMessage();
//            this.hasUser = registerUser(update);
//            Long chatId = hasUser.getChatId();
//            boolean hasText = message.hasText();
//            if (hasText) {
//                SendMessage sendMessage = new SendMessage();
//                sendMessage.setChatId(chatId);
//                if (message.getText().equals("/start") || hasUser.getStatus().equals(STATUS.START.name())) {
//                    sendMessage.setText("Assalamu alaykum " + message.getFrom().getFirstName() + " botga xush kelibsiz!");
//                    execute(sendMessage);
//                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//                    replyKeyboardMarkup.setResizeKeyboard(true);
//                    List<KeyboardRow> keyboard = new ArrayList<>();
////            https://api.telegram.org/bot6137245232:AAE-D41wsTMY6O8ZOTtORyCFt_vutW_SBaw/setWebhook?url=https://be84-213-230-86-58.ngrok-free.app/api/tujjor_bot/public
//
//                    // First row of buttons
//                    KeyboardRow row1 = new KeyboardRow();
//                    row1.add(new KeyboardButton("➕ Yangi Mijoz qo'shish"));
////                row1.add(new KeyboardButton("\uD83D\uDCDD Mijozlar ma’lumotlari"));
//                    keyboard.add(row1);
//
//                    // Second row of buttons
////                KeyboardRow row2 = new KeyboardRow();
////                row2.add(new KeyboardButton("✏\uFE0F Mijoz taxrirlash"));
////                row2.add(new KeyboardButton("\uD83D\uDD0D Mijozni qidirish"));
////                keyboard.add(row2);
//                    replyKeyboardMarkup.setKeyboard(keyboard);
//                    sendMessage.setText("Mijoz qo'shish jarayoni boshlandi!");
//                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
//                    updateStep(STATUS.START_MENU_WAITING);
//                    Message message1 = execute(sendMessage);
//                    addnewClientBtnMessageId = message1.getMessageId();
//
//                } else if (message.getText().equals(newClient) && hasUser.getStatus().equals(STATUS.START_MENU_WAITING.name())) {
////            add customer category process
//                    sendMessage.setText("\uD83E\uDDE9 Mijoz faoliyat turini tanlang");
//                    InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(defaultPage, defaultSize, "category");
//                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//                    Message execute = execute(sendMessage);
//                    updateCustomerCategoryBtnsId = execute.getMessageId();
//                    updateStep(STATUS.ADD_NEW_CLIENT);
//                    DeleteMessage deleteMessage = new DeleteMessage();
//                    deleteMessage.setChatId(chatId);
//                    deleteMessage.setMessageId(addnewClientBtnMessageId);
//                    execute(deleteMessage);
//                } else if (hasUser.getStatus().equals(STATUS.SEND_TERRITORIES.name())) {
//                    sendMessage.setText("Mijoz ism familyasi: " + message.getText());
//                    execute(sendMessage);
//                    client.setName(message.getText());
//                    sendMessage.setText("\uD83D\uDCDD Mijoz addressini yozing: \n" + "Namuna:Buxoro sh,Mustaqillik 32/1 -uy");
//                    execute(sendMessage);
//                    updateStep(STATUS.SEND_FULL_NAME);
//                } else if (hasUser.getStatus().equals(STATUS.SEND_FULL_NAME.name())) {
////                boolean validAddress = isValidAddress(message.getText());
////                if (!validAddress) {
////                    sendMessage.setText("\uD83D\uDCDD Mijoz addresi xato yuborildi!: \n" + "Namuna:Buxoro sh,Mustaqillik 32/1 -uy");
////                    sendMessage.setChatId(hasUser.getChatId());
////                    execute(sendMessage);
////                    return;
////                }
//                    sendMessage.setText("\uD83D\uDCDD Mijoz addressi: " + message.getText());
//                    execute(sendMessage);
//                    client.setAddress(message.getText());
//                    sendMessage.setText("\uD83D\uDCDE Mijoz telefon raqamini kiriting: \n" + "Namuna: +998901234567");
//                    execute(sendMessage);
//                    updateStep(STATUS.SEND_ADDRESS);
//                } else if (hasUser.getStatus().equals(STATUS.SEND_ADDRESS.name())) {
//                    boolean validPhoneNumber = isValidPhoneNumber(message.getText());
//                    if (!validPhoneNumber) {
//                        sendMessage.setText("\uD83D\uDCDE Mijoz telefon raqami noto'g'ri kiritildi! \n" + "Namuna: +998901234567");
//                        execute(sendMessage);
//                        return;
//                    }
//                    InlineKeyboardMarkup inlineKeyboardMarkup = generateDialogBtn("phone");
//                    sendMessage.setText("Tasdiqlang!\n" + "\uD83D\uDCDE Mijoz telefon raqami: " + message.getText());
//                    phone = message.getText();
//                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//                    Message message1 = execute(sendMessage);
//                    deleteDialogPhoneBtnsId = message1.getMessageId();
//                } else if (hasUser.getStatus().equals(STATUS.SEND_PHONE.name())) {
//                    InlineKeyboardMarkup inlineKeyboardMarkup = generateDialogBtn("Inn");
//                    sendMessage.setText("Tasdiqlang!\n" + "\uD83D\uDCC3 Mijoz Inn si: " + message.getText());
//                    Inn = message.getText();
//                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//                    Message message1 = execute(sendMessage);
//                    deleteDialogINNBtnsId = message1.getMessageId();
//                } else if (message.hasText() && hasUser.getStatus().equals(STATUS.SEND_LOCATION.name())) {
//                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//                    replyKeyboardMarkup.setResizeKeyboard(true);
//                    List<KeyboardRow> keyboard = new ArrayList<>();
//                    KeyboardRow row1 = new KeyboardRow();
//                    row1.add(new KeyboardButton("➕ Yangi Mijoz qo'shish"));
//                    keyboard.add(row1);
//                    replyKeyboardMarkup.setKeyboard(keyboard);
//                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
//                    sendMessage.setText("Yangi Mijoz qo'shish uchun pastdagi tugmani bosing: ");
//                    Message message1 = execute(sendMessage);
//                    addnewClientBtnMessageId = message1.getMessageId();
//                    updateStep(STATUS.START);
//                }
//            } else if (message.hasLocation() && hasUser.getStatus().equals(STATUS.SEND_INN.name())) {
//                SendMessage sendMessage = new SendMessage();
//                sendMessage.setChatId(chatId);
//                location = message.getLocation();
//                longitude = location.getLongitude();
//                latitude = location.getLatitude();
//                InlineKeyboardMarkup inlineKeyboardMarkup = generateDialogBtn("location");
//                sendMessage.setText("Tasdiqlang!\n" + "\uD83D\uDCC3 Mijoz locationi: " + location.getLongitude() + " " + location.getLatitude());
//                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//                Message message1 = execute(sendMessage);
//                deleteDialogLocationBtnsId = message1.getMessageId();
//            }
////            else if (message.getText().equals(clientData)) {
////
////            } else if (message.getText().equals(updateClient)) {
////
////            } else if (message.getText().equals(searchClient)) {
////
////            }
//        } else if (update.hasCallbackQuery()) {
//            CallbackQuery callbackQuery = update.getCallbackQuery();
//            this.hasUser = registerUser(update);
//            Long chatId = hasUser.getChatId();
//            String btnText = callbackQuery.getData();
//            SendMessage sendMessage = new SendMessage();
//            sendMessage.setChatId(chatId);
//            if (hasUser.getStatus().equals(STATUS.ADD_NEW_CLIENT.name()) && btnText.substring(btnText.lastIndexOf(":") + 1).equals("category")) {
//                if (btnText.startsWith("next:")) {
//                    EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
//                    editMessageReplyMarkup.setChatId(hasUser.getChatId());
//                    String numberString = btnText.replaceAll("\\D+", "");  // Remove non-digit characters
//                    defaultPage = Integer.parseInt(numberString);
//                    InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(defaultPage, defaultSize, "category");
//                    editMessageReplyMarkup.setMessageId(updateCustomerCategoryBtnsId);
//                    editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);
//                    execute(editMessageReplyMarkup);
//                } else if (btnText.startsWith("previous:")) {
//                    String numberString = btnText.replaceAll("\\D+", "");  // Remove non-digit characters
//                    defaultPage = Integer.parseInt(numberString);
//                    InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(defaultPage, defaultSize, "category");
//                    EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
//                    editMessageReplyMarkup.setChatId(hasUser.getChatId());
//                    editMessageReplyMarkup.setMessageId(updateCustomerCategoryBtnsId);
//                    editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);
//                    execute(editMessageReplyMarkup);
//                }
//            } else if (hasUser.getStatus().equals(STATUS.SEND_CUSTOMER_CATEGORIES.name()) && btnText.substring(btnText.lastIndexOf(":") + 1).equals("territory")) {
//                if (btnText.startsWith("next:")) {
//                    EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
//                    editMessageReplyMarkup.setChatId(hasUser.getChatId());
//                    String numberString = btnText.replaceAll("\\D+", "");  // Remove non-digit characters
//                    defaultPageTerritory = Integer.parseInt(numberString);
//                    InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(defaultPageTerritory, defaultSize, "territory");
//                    editMessageReplyMarkup.setMessageId(updateTerritoryBtnsId);
//                    editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);
//                    execute(editMessageReplyMarkup);
//                } else if (btnText.startsWith("previous:")) {
//                    String numberString = btnText.replaceAll("\\D+", "");  // Remove non-digit characters
//                    defaultPageTerritory = Integer.parseInt(numberString);
//                    InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(defaultPageTerritory, defaultSize, "territory");
//                    EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
//                    editMessageReplyMarkup.setChatId(hasUser.getChatId());
//                    editMessageReplyMarkup.setMessageId(updateTerritoryBtnsId);
//                    editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);
//                    execute(editMessageReplyMarkup);
//                }
//            } else if (btnText.startsWith("category:") && hasUser.getStatus().equals(STATUS.ADD_NEW_CLIENT.name())) {
//                String categoryName = btnText.split(":")[1];
//                CustomerCategory savedCustomerCategory = null;
//                List<CustomerCategory> customerCategories = customerCategoryRepository.findAll();
//                for (CustomerCategory customerCategory : customerCategories) {
//                    if (categoryName.equals(customerCategory.getName())) {
//                        savedCustomerCategory = customerCategoryRepository.findByName(categoryName).orElseThrow(() -> new NoSuchElementException("not found category"));
//                        client.setCategory(customerCategory);
//                    }
//                }
//                sendMessage.setText("\uD83E\uDDE9 Mijoz faoliyat turi: " + savedCustomerCategory.getName() + "\n");
//                execute(sendMessage);
//                sendMessage.setText(" \uD83C\uDFD8 Mijoz uchun hudud tanlang");
//                InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup(defaultPageTerritory, defaultSize, "territory");
//                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//                Message message = execute(sendMessage);
//                updateStep(STATUS.SEND_CUSTOMER_CATEGORIES); //step
//                updateTerritoryBtnsId = message.getMessageId();
//                DeleteMessage deleteMessage = new DeleteMessage();
//                deleteMessage.setChatId(hasUser.getChatId());
//                deleteMessage.setMessageId(updateCustomerCategoryBtnsId);
//                execute(deleteMessage);
//
//            } else if (btnText.startsWith("territory:") && hasUser.getStatus().equals(STATUS.SEND_CUSTOMER_CATEGORIES.name())) {
//                String territoryName = btnText.split(":")[1];
//                Territory savedTerritory = null;
//                List<Territory> territories = territoryRepository.findAll();
//                for (Territory territory : territories) {
//                    if (territoryName.equals(territory.getName())) {
//                        savedTerritory = territoryRepository.findByName(territoryName).orElseThrow(() -> new NoSuchElementException("not found category"));
//                        client.setTerritory(savedTerritory);
//                    }
//                }
//                sendMessage.setText("\uD83C\uDFD8 Mijoz uchun hudud: " + savedTerritory.getName() + "\n");
//                execute(sendMessage);
//                sendMessage.setText("Mijoz ism,familyasini kiriting: \n" + "Namuna:Aliyev Vali");
//                execute(sendMessage);
//                updateStep(STATUS.SEND_TERRITORIES); //step
//                DeleteMessage deleteMessage = new DeleteMessage();
//                deleteMessage.setChatId(hasUser.getChatId());
//                deleteMessage.setMessageId(updateTerritoryBtnsId);
//                execute(deleteMessage);
//            } else if (btnText.startsWith("phone:") && hasUser.getStatus().equals(STATUS.SEND_ADDRESS.name())) {
//                String dialogBtnText = btnText.substring(btnText.indexOf(":") + 1);
//                if (dialogBtnText.equals("refuse")) {
//                    sendMessage.setText("\uD83D\uDCDE Mijoz telefon raqamini kiriting: \n" + "Namuna: +998901234567");
//                    execute(sendMessage);
//                } else if (dialogBtnText.equals("accept")) {
//                    sendMessage.setText("telefon raqam: " + phone);
//                    execute(sendMessage);
//                    client.setPhone(phone);
//                    sendMessage.setText("\uD83D\uDCC3 Mijoz Inn sini kiriting: \n" + "Namuna:123567");
//                    execute(sendMessage);
//                    updateStep(STATUS.SEND_PHONE);
//                    DeleteMessage deleteMessage = new DeleteMessage();
//                    deleteMessage.setChatId(hasUser.getChatId());
//                    deleteMessage.setMessageId(deleteDialogPhoneBtnsId);
//                    execute(deleteMessage);
//                }
//            } else if (btnText.startsWith("Inn:") && hasUser.getStatus().equals(STATUS.SEND_PHONE.name())) {
//                String dialogBtnText = btnText.substring(btnText.indexOf(":") + 1);
//                if (dialogBtnText.equals("refuse")) {
//                    sendMessage.setText("\uD83D\uDCC3 Mijoz Inn sini kiriting: \n" + "Namuna:123567");
//                    execute(sendMessage);
//                } else if (dialogBtnText.equals("accept")) {
//                    sendMessage.setText("Inn: " + Inn);
//                    execute(sendMessage);
//                    client.setTin(Inn);
//                    sendMessage.setText("\uD83D\uDCCC Mijoz locationini yuboring:");
//                    execute(sendMessage);
//                    updateStep(STATUS.SEND_INN);
//                    DeleteMessage deleteMessage = new DeleteMessage();
//                    deleteMessage.setChatId(hasUser.getChatId());
//                    deleteMessage.setMessageId(deleteDialogINNBtnsId);
//                    execute(deleteMessage);
//                }
//            } else if (btnText.startsWith("location:") && hasUser.getStatus().equals(STATUS.SEND_INN.name())) {
//                String dialogBtnText = btnText.substring(btnText.indexOf(":") + 1);
//                if (dialogBtnText.equals("refuse")) {
//                    sendMessage.setText("\uD83D\uDCCC Mijoz locationini yuboring:");
//                    execute(sendMessage);
//                } else if (dialogBtnText.equals("accept")) {
//                    sendMessage.setText("\uD83D\uDCC3 Mijoz locationi: " + location.getLongitude() + " " + location.getLatitude());
//                    execute(sendMessage);
//                    client.setLongitude(longitude);
//                    client.setLatitude(latitude);
//                    client.setRegistration_date(Timestamp.valueOf(LocalDateTime.now()));
//                    clientRepository.save(client);
//                    sendMessage.setText("✅ Mijoz muvaffaqiyatli qo'shildi");
//                    execute(sendMessage);
//                    updateStep(STATUS.SEND_LOCATION);
//                    defaultPage = 0;
//                    defaultPageTerritory = 0;
//                    DeleteMessage deleteMessage = new DeleteMessage();
//                    deleteMessage.setChatId(hasUser.getChatId());
//                    deleteMessage.setMessageId(deleteDialogLocationBtnsId);
//                    execute(deleteMessage);
//                }
//            }
//        }
//    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            this.hasUser = registerUser(update);
            boolean hasText = message.hasText();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            if (hasText) {
                if (message.getText().equals("/start") || hasUser.getStatus().equals(STATUS.START.name())) {
                    if (!hasUser.getStatus().equals(STATUS.BLOCKED.name())) {
                        ReplyKeyboardMarkup shareContactButton = createShareContactButton();
                        sendMessage.setText("Assalamu alaykum agentlar uchun botga xush kelibsiz\n iltimos kontakingizni yuboring");
                        sendMessage.setReplyMarkup(shareContactButton);
                        updateStep(STATUS.START_MENU_WAITING);
                    } else {
                        sendMessage.setText("siz ushbu botdan foydalana olmaysiz!");
                    }
                    execute(sendMessage);
                } else if (hasUser.getStatus().equals(STATUS.SEND_PASSWORD.name())) {
                    if (hasAgent.getPassword().equals(message.getText())) {
                        hasAgent.setChatId(chatId);
                        agentRepository.save(hasAgent);
//                        btnlar chiziladi
                        ReplyKeyboardMarkup replyKeyboardMarkup = generateMenuBtn();
                        sendMessage.setText("asosiy menu");
                        sendMessage.setReplyMarkup(replyKeyboardMarkup);
                        updateStep(STATUS.AGENT_MENU);
                    } else {
                        sendMessage.setText("parol noto'g'ri kiritildi");
                        updateStep(STATUS.BLOCKED);
                    }
                    execute(sendMessage);
                } else if (hasUser.getStatus().equals(STATUS.BLOCKED.name())) {
                    sendMessage.setText("siz ushbu botdan foydalana olmaysiz!");
                    execute(sendMessage);
                }
            } else if (message.hasContact() && hasUser.getStatus().equals(STATUS.START_MENU_WAITING.name())) {
                String phoneNumber = message.getContact().getPhoneNumber();
                try {
                    hasAgent = agentRepository.findByPhone(phoneNumber).orElseThrow(() -> new NoSuchElementException());
                    if (hasAgent != null) {
                        sendMessage.setText("parolingizni kiritng hurmatli agent");
                        updateStep(STATUS.SEND_PASSWORD);
                    }
                } catch (NoSuchElementException e) {
                    sendMessage.setText("kechirasiz siz ushbu botdan foydalana olmaysiz!");
                    telegramUserRepository.delete(hasUser);
                }
                execute(sendMessage);
            }
        }
    }

    private ReplyKeyboardMarkup createShareContactButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardButton shareContactButton = new KeyboardButton();
        shareContactButton.setText("shareContact");
        shareContactButton.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(shareContactButton);

        replyKeyboardMarkup.setKeyboard(List.of(row));

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup generateMenuBtn() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        // First row of buttons
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("➕ Yangi Mijoz qo'shish"));
        keyboard.add(row1);

        // Second row of buttons
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("\uD83D\uDC68\u200D\uD83D\uDCBB Mijozlar"));
        keyboard.add(row2);

        // thrid row of buttons
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("\uD83D\uDCCD Kartadagi Mijozlar"));
        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private TelegramUser registerUser(Update update) {
        Message message = update.getMessage();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId;
        if (update.hasCallbackQuery()) {
            chatId = callbackQuery.getMessage().getChatId();
        } else {
            chatId = message.getChatId();
        }
        Optional<TelegramUser> user = telegramUserRepository.findByChatId(chatId);
        return user.orElseGet(() -> telegramUserRepository.save(TelegramUser.builder()
                .id(null)
                .chatId(chatId)
                .status(STATUS.START.name())
                .build()
        ));
    }

    private void updateStep(STATUS nextStep) {
        hasUser.setStatus(nextStep.name());
        telegramUserRepository.save(hasUser);
    }


//    private InlineKeyboardMarkup getInlineKeyboardMarkup(Integer page, Integer size, String callbackPrefix) {
//        Pageable pageable = PageRequest.of(page > 0 ? page : 0, size);
//        if (callbackPrefix.equals("category")) {
//            Page<CustomerCategory> customerCategories = customerCategoryRepository.findAllCustomerCategoriesForBot(pageable);
//            return InlineKeyboardUtils.createInlineKeyboard(customerCategories, callbackPrefix);
//        } else if (callbackPrefix.equals("territory")) {
//            Page<Territory> territories = territoryRepository.findAllTerritoriesForBot(pageable);
//            return InlineKeyboardUtils.createInlineKeyboard(territories, callbackPrefix);
//        }
//        return null;
//    }

    //    keyinchalik ishlatamiz
    public boolean isValidAddress(String address) {
        // You can modify this pattern to match your specific address format
        String addressPattern = ".*Buxoro\\\\ssh,\\\\sMustaqillik\\\\s32/1\\\\s-uy.*";
        Pattern pattern = Pattern.compile(addressPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(address);
        return matcher.matches();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        // You can modify this pattern to match your specific phone number format
        String phonePattern = "\\+998\\d{9}";
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean isValidFullName(String fullName) {
        // You can modify this pattern to match your specific name format
        String namePattern = "^[A-Z][a-z]+\\s+[A-Z][a-z]+$";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(fullName);
        return matcher.matches();
    }

    public InlineKeyboardMarkup generateDialogBtn(String callbackPrefix) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton ha = new InlineKeyboardButton();
        ha.setText("✅ ha");
        ha.setCallbackData(callbackPrefix + ":accept");
        row.add(ha);
        InlineKeyboardButton yuq = new InlineKeyboardButton();
        yuq.setText("❌ yo'q");
        yuq.setCallbackData(callbackPrefix + ":refuse");
        row.add(yuq);
        keyboard.add(row);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboard);
        return inlineKeyboardMarkup;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}
