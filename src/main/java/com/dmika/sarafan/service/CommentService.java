package com.dmika.sarafan.service;

import com.dmika.sarafan.domain.Comment;
import com.dmika.sarafan.domain.User;
import com.dmika.sarafan.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment create(Comment comment, User author){

        comment.setAuthor(author);
        commentRepository.save(comment);

        return comment;

    }

}
