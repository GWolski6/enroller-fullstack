<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],

        data() {
            return {
                meetings: []
            };
        },

        methods:
        {
            addNewMeeting(meeting) {this.$http.post('meetings', meeting).then(() => this.listUpdate());},
            addMeetingParticipant(meeting) {this.$http.post('meetings/' + meeting.id + '/' + this.username).then(response => this.listUpdate());},
            removeMeetingParticipant(meeting) {this.$http.delete('meetings/' + meeting.id + '/' + this.username).then(response => this.listUpdate());},
            deleteMeeting(meeting) {this.$http.delete('meetings/' + meeting.id).then(() => this.listUpdate());},
            listUpdate() {this.$http.get('meetings').then(response => {this.meetings = response.body;})}
        },

        mounted()
        {
            this.listUpdate();
        }
    }
</script>
