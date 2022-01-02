package com.project.petboard.domain.post;

import com.project.petboard.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Getter
public class PostRequestDto {

    private Long memberNumber;

    private String postTitle;

    private String postContents;

    private String postCategory;

    public PostRequestDto(Long memberNumber,String postTitle,String postContents,String postCategory){
        this.memberNumber = memberNumber;
        this.postTitle = postTitle;
        this.postContents = postContents;
        this.postCategory = postCategory;
    }

    public Post toEntity(Member member){
        return Post.builder()
                .member(member)
                .postTitle(postTitle)
                .postCategory(postCategory)
                .postContents(postContents)
                .build();
    }
}
