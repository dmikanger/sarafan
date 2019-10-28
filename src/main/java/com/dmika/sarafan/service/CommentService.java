package com.dmika.sarafan.service;

import com.dmika.sarafan.domain.Comment;
import com.dmika.sarafan.domain.User;
import com.dmika.sarafan.domain.Views;
import com.dmika.sarafan.dto.EventType;
import com.dmika.sarafan.dto.ObjectType;
import com.dmika.sarafan.repository.CommentRepository;
import com.dmika.sarafan.util.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BiConsumer<EventType, Comment> wsSender;

    @Autowired
    public CommentService(CommentRepository commentRepository, WsSender wsSender) {
        this.commentRepository = commentRepository;
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
    }

    public Comment create(Comment comment, User author){

        comment.setAuthor(author);
        Comment commentFromDb = commentRepository.save(comment);

        wsSender.accept(EventType.CREATE, commentFromDb);

        return commentFromDb;

    }

}
