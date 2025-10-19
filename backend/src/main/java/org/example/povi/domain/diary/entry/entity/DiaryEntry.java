package org.example.povi.domain.diary.entry.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.povi.domain.diary.type.MoodEmoji;
import org.example.povi.domain.diary.type.Visibility;
import org.example.povi.domain.user.entity.User;
import org.example.povi.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "diary_entries")
@AttributeOverride(name = "id", column = @Column(name = "diary_id"))
public class DiaryEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood_emoji", nullable = false)
    private MoodEmoji moodEmoji = MoodEmoji.NEUTRAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private Visibility visibility = Visibility.PRIVATE;

    @OneToMany(mappedBy = "diaryEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryImage> images = new ArrayList<>();

    @Builder
    public DiaryEntry(User user, String title, String content, MoodEmoji moodEmoji, Visibility visibility) {

        this.user = user;
        this.title = title;
        this.content = content;
        this.moodEmoji = (moodEmoji != null) ? moodEmoji : MoodEmoji.NEUTRAL;
        this.visibility = (visibility != null) ? visibility : Visibility.PRIVATE;
    }

    public void renameTo(String newTitle) { this.title = newTitle; }
    public void rewriteContent(String newContent) { this.content = newContent; }
    public void changeMood(MoodEmoji newMood) { this.moodEmoji = newMood; }
    public void changeVisibility(Visibility newVisibility) { this.visibility = newVisibility; }

    public void addImage(DiaryImage image) {
        images.add(image);
        image.setDiaryEntry(this);
    }

    public void replaceImages(List<String> urls) {
        images.clear();
        for (String url : urls) {
            addImage(new DiaryImage(this, url));
        }
    }

}

