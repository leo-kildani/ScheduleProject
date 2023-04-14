import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
 
 /*
 *@author Leo K
 *@date Apr 10, 2023
 */
 
 public class Schedule {
     public class Job {
         public int startTime, finishTime, initTime;
         public List<Job> incoming, outgoing;
         public int inDeg;
 
         private Job(int initialTime) {
             startTime = 0;
             initTime = finishTime = initialTime;
             incoming = new ArrayList<>();
             outgoing = new ArrayList<>();
         }
 
         public void requires(Job o) {
             incoming.add(o);
             o.outgoing.add(this);
             needToFinish = true;
		}
 
         public int start() {
        	 if (needToFinish)
        		 finish();
        	 return (inDeg > 0) ? -1 : startTime;
         }
         
         public void relaxJob(Job req) {
        	 startTime = Math.max(startTime, req.finishTime);
        	 finishTime = startTime + initTime;
         }
         
     }
 
     private List<Job> jobs;
     private boolean needToFinish;
     private int lastFinish;
     
 
     public Schedule() {
         jobs = new ArrayList<>();
         needToFinish = true;
     }
 
     public Job insert(int initialTime) {
         Job newJob = new Job(initialTime);
         jobs.add(newJob);
         return newJob;
     }
 
     public Job get(int idx) {
         return jobs.get(idx);
     }
 
     public int finish() {
    	 if (!needToFinish) {
    		 return lastFinish;
    	 }
    	 
    	 // get jobs in top order first
    	 List<Job> jobsTopSorted = topSort();
    	 
    	 // loop through jobs in top order
    	 for (Job j: jobsTopSorted) {
    		 lastFinish = Math.max(lastFinish, j.finishTime);
    		 // loop through outgoing jobs
    		 for (Job o: j.outgoing) {
    			 o.relaxJob(j);
    		 }
    	 }
    	 
    	 if (jobsTopSorted.size() != jobs.size()) {
    		 lastFinish = -1;
    	 }
    	 
    	 needToFinish = false;
    	 return lastFinish;
     }
 
     private List<Job> topSort() {
    	 initSchedule();
    	 List<Job> ordered = new ArrayList<>();
    	 Queue<Job> q = new LinkedList<>();
    	 
    	 // add all jobs with no required jobs
         for (Job j: jobs) {
        	 if (j.inDeg == 0) 
        		 q.add(j);
         }
         
         // loop through ordered & remove outgoing jobs until next job w/ 0 inDeg is found
         while (!q.isEmpty()) {
             Job currJob = q.poll(); // next job in queue
             for (Job j : currJob.outgoing) {
                 if (--j.inDeg == 0)
                     q.add(j);
             }
             ordered.add(currJob);
         }

         return ordered;
     }
 
     private void initSchedule() {
         for (Job j : jobs) {
             j.inDeg = j.incoming.size();
         }
     }
 }