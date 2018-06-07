package com.under.discord.session.domain;

import net.dv8tion.jda.core.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static com.under.discord.session.domain.VoiceChannelEvent.ENTER;
import static com.under.discord.session.domain.VoiceChannelEvent.LEAVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SessionTest {

    Session session;
    
    @Mock
    User user;
    
    @Test
    public void should_start_session() {
        LocalDate startDate = LocalDate.now();
        given( user.getName() ).willReturn( "john" );
        session = new Session(startDate);

        session.declarePresence(user);

        assertThat( session.getStartDate() ).isEqualTo(startDate);
        assertThat( session.getUserJoins() ).contains(user);
        Map<String, TimeTracker> timeTrackers = session.getUserTimeTrackers();
        assertThat( timeTrackers )
                .hasSize(1)
                .containsKeys("john");
        List<UserTimeTrack> johnTimeTracks = timeTrackers.get("john").getTimeTracks();
        assertThat( johnTimeTracks.get(0).getEvent() ).isEqualTo( ENTER );
        assertThat( johnTimeTracks.get(0).getDateTime() )
                .isBetween(startDate.atStartOfDay(), startDate.atStartOfDay().plus(1, ChronoUnit.DAYS));
    }
    
    @Test
    public void should_compute_spentTime_when_stop_session() throws InterruptedException {
        LocalDate startDate = LocalDate.now();
        given( user.getName() ).willReturn( "john" );
        session = new Session(startDate);
        
        session.declarePresence(user);
        Thread.sleep(2000L);
        session.stop();

        Map<String, TimeTracker> timeTrackers = session.getUserTimeTrackers();
        Long timeSpent = session.getSessionTimeFor("john");
        TimeTracker johnTimeTracker = timeTrackers.get("john");

        UserTimeTrack firstEventTrack = johnTimeTracker.getTimeTracks().get(0);
        UserTimeTrack secondEventTrack = johnTimeTracker.getTimeTracks().get(1);
        assertThat( firstEventTrack.getEvent() ).isEqualTo(ENTER);
        assertThat( secondEventTrack.getEvent() ).isEqualTo(LEAVE);
        assertThat( firstEventTrack.getDateTime() ).isLessThan( secondEventTrack.getDateTime() );
        assertThat( timeSpent ).isEqualTo(2);
    }
    
    @Test
    public void should_compute_spentTime_when_userLeave_and_stop_session() throws InterruptedException {
        LocalDate startDate = LocalDate.now();
        User smith = mock(User.class);
        given( user.getName() ).willReturn( "john" );
        given( smith.getName() ).willReturn( "smith" );
        session = new Session(startDate);

        session.declarePresence(user);
        session.declarePresence(smith);
        Thread.sleep(2000L);
        session.declareLeave(smith);
        session.stop();

        Long johnTimeSpent = session.getSessionTimeFor("john");
        Long smithTimeSpent = session.getSessionTimeFor("smith");
        assertThat( johnTimeSpent ).isEqualTo(2);
        assertThat( smithTimeSpent ).isEqualTo(2);
    }
}