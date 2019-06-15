#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>

const int NUM_THREADS =   50;
pthread_mutex_t lock;

void *PrintHello(void *threadid)
{
   long tid;
   tid = (long)threadid;
   pthread_mutex_lock(&lock);
   printf("Hello World! It's me, thread #%ld!\n", tid);
   printf("  Part 2 of the message from thread #%ld!\n", tid);
   pthread_mutex_unlock(&lock);
   pthread_exit(NULL);
}

int main (int argc, char *argv[])
{
   pthread_t threads[NUM_THREADS];
   int rc;
   long t;
   for(t=0; t<NUM_THREADS; t++){
      printf("In main: creating thread %ld\n", t);
      rc = pthread_create(&threads[t], NULL, PrintHello, (void *)t);
      if (rc){
         printf("ERROR; return code from pthread_create() is %d\n", rc);
         exit(-1);
      }
   }

   /* Last thing that main() should do */
   pthread_mutex_destroy(&lock);
   pthread_exit(NULL);
}

