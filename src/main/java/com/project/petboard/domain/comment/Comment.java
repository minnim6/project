package com.project.petboard.domain.comment;

import com.project.petboard.domain.board.Board;
import com.project.petboard.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Comment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long commentNumber;

    @ManyToOne
    @JoinColumn
    private Board board;

    @ManyToOne
    @JoinColumn
    private Member member;

    private String commentContents;

    @Temporal(value = TemporalType.DATE) //년월일 date 타입 db에 매핑
    @Column(insertable = true, updatable = false)
    @CreationTimestamp
    private Date commentCreateDate;

    @Temporal(value = TemporalType.DATE)
    @Column(insertable = true,updatable = true)
    @CreationTimestamp
    private Date commentAmendDate;

    @Builder
    public Comment(Board board,Member member,String commentContents){
        this.board = board;
        this.member = member;
        this.commentContents = commentContents;
    }

    public void update(String commentContents){
        this.commentContents = commentContents;
    }
}
