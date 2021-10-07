# A Verification-Oriented Concurrent Web Crawler

As part of our course (concurrent programming in java) project, we built a concurrent web crawler designed to take in a seed list of Universal Resource
Locators (URLs) and parse the HTML to find further embedded URLs recursively. The aim is to build the most efficient crawler possible, crawling the most number of URLs per hour. We also built a CSP# concurrency model with PAT for the abstracted problem to check some concurrency properties and we used obtained traces for concurrency replay.

## Description

### The design of our concurrent URL crawler
Our crawler’s fundamental blueprint can be described in the following figure:
![alt text](https://github.com/MarahGamdou/Java-Url-Crawler/blob/master/general%20structure.png)

* Url list: Seed URL List 
* BUL: Buffered URL List
* IUT: Indexed URL Tree

### Concurrency  Problem: Avoiding data race and deadlocks 
#### Avoiding Data Race 
* **Crawling Thread (CT)**
  * If a CT takes the lock on the BUL, it checks if the BUL is full.
	  * If it is full
		  it releases the lock and notifyAll so the IBT can take the lock
	  * If it is not full
      it writes new found URLs into available space and releases the lock and notifyall so the IBT or the other CT can take the lock
* **Index Building Thread (IBT)**
  * If the IBT takes the lock on the BUL, it checks if the BUL is full
    * If it is not full
      it releases the lock and notifyAll  so the CTs can take the lock
    * If the BUL is full
      it holds on to the lock on  the BUL till it can acquire the lock on the IUT
      Once it has both locks, it adds all entries into the IUT ensuring there are no duplicates and releases the lock on the IUT and notifies other IBTs. Then it clears the BUL and releases the lock on the BUL and notifyall the CTs.

#### Avoiding Deadlocks 
* Since the CTs only need the lock on the BUL without requiring the lock on the IUT, the CT won't cause a deadlock.

  we avoid this  situation: CT holds on the lock of IUT and waits for the lock of BUL to be released and IBT holds on the lock of BUL  and waits for the lock of IUT  to be released. 
  
* Since the IBT must have already have the lock on the BUL before acquiring the lock on the IUT, it will be able to complete operations without causing a deadlock.
  
  we avoid this situation: an IBT holds on the lock of IUT and waits for the lock of BUL to be released  and the two other IBTs hold on the lock of  BUL and wait for for the lock of IUT  to be released 

## Authors

Contributors names and contact info

* GAMDOU Marah 
* J. K. SHAOWEI
* C. S. WEI
* C. H. YUN. 


## License

[MIT](./LICENSE)

