package com.dmika.sarafan.service;

import com.dmika.sarafan.domain.Message;
import com.dmika.sarafan.domain.User;
import com.dmika.sarafan.domain.Views;
import com.dmika.sarafan.dto.EventType;
import com.dmika.sarafan.dto.MessagePageDto;
import com.dmika.sarafan.dto.MetaDto;
import com.dmika.sarafan.dto.ObjectType;
import com.dmika.sarafan.repository.MessageRepository;
import com.dmika.sarafan.util.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageService {

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepository messageRepository;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageService(MessageRepository messageRepository, WsSender wsSender) {
        this.messageRepository = messageRepository;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }



    private void fillMeta(Message message) throws IOException {

        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()){
            String url = text.substring(matcher.start(), matcher.end());

            matcher = IMG_REGEX.matcher(url);
            message.setLink(url);


            if (matcher.find()){
                message.setLinkCover(url);
            }else if (!url.contains("youtu")) {
                MetaDto meta = getMeta(url);

                message.setLinkCover(meta.getCover());
                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
            }

        }

    }

    private MetaDto getMeta(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();
        Elements title = doc.select("meta[name$=title], meta[property$=title]");
        Elements description = doc.select("meta[name$=description], meta[property$=description]");
        Elements cover = doc.select("meta[name$=image], meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }

    public void delete(Message message) {
        messageRepository.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    public Message update(Message messageFromDb, Message message) throws IOException {

        //BeanUtils.copyProperties(messageFromDb, message, "id");
        messageFromDb.setText(message.getText());
        fillMeta(messageFromDb);
        Message updateMessage = messageRepository.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updateMessage);

        return updateMessage;

    }

    public Message create(Message message, User user) throws IOException {

        message.setCreationDate(LocalDateTime.now());
        fillMeta(message);
        message.setAuthor(user);
        Message updateMessage = messageRepository.save(message);

        wsSender.accept(EventType.CREATE, updateMessage);

        return updateMessage;

    }

    public MessagePageDto findAll(Pageable pageable) {
        Page<Message> page = messageRepository.findAll(pageable);
        return new MessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );

    }
}
