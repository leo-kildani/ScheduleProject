 import java.util.ArrayList;
 import java.util.List;
 
 /*
 *@author Leo K
 *@date Apr 10, 2023
 */
 
 public class Schedule {
     public class Job {
         public int startTime, finishTime;
         public boolean started, finished;
         public List<Job> incoming, outgoing;
 
         private Job(int timeToComplete) {
             startTime = 0;
             this.finishTime = timeToComplete;
             started = finished = false;
             incoming = new ArrayList<>();
             outgoing = new ArrayList<>();
         }
 
         public void requires(Job o) {
             incoming.add(o);
             o.outgoing.add(this);
             // only need to initialize everytime a new job is added; otherwise, start and end times stay the same
             initSchedule();
         }
 
         public int start() {
             if (finished) {
                 return startTime;
             }
             
             if (incoming.isEmpty()) {
                 started = true;
                 finished = true;
                 return startTime;
             }
             
             started = true;
             
             for (Job j: incoming) {
                 if (j.started && !j.finished || j.start() < 0) return -1;
                 int diffTime = j.finishTime - startTime;
                 // determine whether the constraint is larger than current startTime
                 if (diffTime > 0) {
                     startTime += diffTime;
                     finishTime += diffTime;
                 }
             }
             
             finished = true;
             
             return startTime;
         }
         
         private void initSchedule() {
             if (started) {
                 started = finished = false;
                 for (Job j: outgoing) {
                     j.initSchedule();
                 }
             }
         }
     }
 
     // Start of GraphImpl
     private List<Job> jobs;
 
     public Schedule() {
         jobs = new ArrayList<>();
     }
 
     public Job insert(int endTime) {
         Job newJob = new Job(endTime);
         jobs.add(newJob);
         return newJob;
     }
 
     public Job get(int idx) {
         return jobs.get(idx);
     }
 
     public int finish() {
         int timeToFinish = 0;
 
         // loop through reversed topOrder
         for (Job j : jobs) {
             if (j.start() < 0) return -1;
             timeToFinish = Math.max(timeToFinish, j.finishTime);
         }
 
         return timeToFinish;
     }
 }