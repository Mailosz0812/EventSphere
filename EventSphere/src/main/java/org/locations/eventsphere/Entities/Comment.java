package org.locations.eventsphere.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "COMMENT_T")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @SequenceGenerator(name = "comment_seq", sequenceName = "COMMENT_SEQ", allocationSize = 1)
    private Long COMMENTID;
    private String CONTENT;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private LoggedUser user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENTID", referencedColumnName = "EVENTID")
    private Event event;
}
