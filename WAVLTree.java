package ex1;

/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */

public class WAVLTree {
	private WAVLNode root;
	
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
    return this.root == null; // to be replaced by student code
  }


  private static String searchHelper(int k, IWAVLNode node) {
	  if (node == null) {
		  return null;
	  }
	  else if (node.getKey() == k) {
		  return node.getValue();
	  }
	  else if (k < node.getKey()) {
		  return searchHelper(k,node.getLeft());
	  }
	  else {
		  return searchHelper(k,node.getRight());
	  }
  }
  /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k){
	  return searchHelper(k,root);

  }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
  
   public int insert(int k, String i) {
	   if (this.root == null) {
		   this.root = new WAVLNode(k,i); 
		   return 0;
	   }
	   WAVLNode insertionPoint = treePosition(this.root,k);// to be replaced by student code
	   if (k == insertionPoint.getKey()) {
		   return -1;	
	   }
	   else {
		increaseSize(insertionPoint);
	    if (k> insertionPoint.getKey()) {
		   insertionPoint.setRight(k, i);
		   insertionPoint.right.setParent(insertionPoint);
	   }
	   else {
		   insertionPoint.setLeft(k, i);
		   insertionPoint.left.setParent(insertionPoint);
	   }
	   }
	   return balaceHelperInsertion(insertionPoint);
   }
   private void increaseSize(WAVLNode insertionPoint) {
	while (insertionPoint != null) {
		insertionPoint.size ++;
		insertionPoint = insertionPoint.parent;
	}
	
}

private int balaceHelperInsertion(WAVLNode node) {
	   return balanceTreeAfterInsertion(node,0);
  }
   

  private int balanceTreeAfterInsertion(WAVLNode node,int numOfOperation) {
	if (node == null) {
		return numOfOperation;
			}
	int rankLeftDiff= node.getRank() - node.left.getRank();
	int rankRightDiff= node.getRank() - node.right.getRank();
	if ((rankRightDiff!=0)&&(rankLeftDiff!=0)){
		return numOfOperation;
		
	}
	if (rankLeftDiff==0) {
		if(rankRightDiff==1) {
			Promote(node);
			numOfOperation++;
			return balanceTreeAfterInsertion(node.parent,numOfOperation);
		}
		else if(rankRightDiff==2) {
			if (node.left.getRank() - node.left.left.getRank() == 1) {
				rotateRight(node);
				numOfOperation++;
				return numOfOperation;
			}
		
			else {
				doubleRotateRight(node);
				numOfOperation+=2;
				return numOfOperation;
			}
	}
	}
			
			if (rankRightDiff==0) {
				if(rankLeftDiff==1) {
					Promote(node);
					numOfOperation++;
					return balanceTreeAfterInsertion(node.parent,numOfOperation);
				}
				else if(rankLeftDiff==2) {
					if (node.right.getRank() - node.right.right.getRank() == 1) {
						rotateLeft(node);
						numOfOperation++;
						return numOfOperation;
					}
					else {
						doubleRotateLeft(node);
						numOfOperation+=2;
						return numOfOperation;
					}
			
				}
	}
	

	return numOfOperation;

  }
  private int balanceTreeAfterDeletion(WAVLNode node, int numOfOperation ) {
	  if (node == null) {
			return numOfOperation;
				}
	  int rankLeftDiff= node.getRank() - node.left.getRank();
	  int rankRightDiff= node.getRank() - node.right.getRank();
	  
	  if (isLeaf(node)) {
		 if (rankLeftDiff ==2 && rankRightDiff==2)  {
			 demote(node);
			 numOfOperation++;
			 return balanceTreeAfterDeletion(node.parent, numOfOperation);
		 }
	  }
	  if (rankLeftDiff!=3 && rankRightDiff!=3) {
		  return numOfOperation;
	  }
	  if (rankLeftDiff == 3) {
		if (rankRightDiff == 2) {
			demote (node);
			numOfOperation++;
			return balanceTreeAfterDeletion(node.parent, numOfOperation);
		}
		else if (rankRightDiff ==1) {
			WAVLNode y = node.right;
			int yRightDiff = y.rank - y.right.rank;
			int yLeftDiff = y.rank - y.left.rank;
			if (yRightDiff == 2 && yLeftDiff==2) {
				y.rank --;
				node.rank --;
				numOfOperation+=2;
				return balanceTreeAfterDeletion(node.parent, numOfOperation);
			}
			else if (yRightDiff ==1) {
				rotateLeft(node);
				node.parent.rank++;
				numOfOperation ++;
				if (isLeaf(node)) {
					rankLeftDiff= node.getRank() - node.left.getRank();
					rankRightDiff= node.getRank() - node.right.getRank(); 
					if (rankLeftDiff ==2 && rankRightDiff==2)  {
						 demote(node);
					 }
				}
				return numOfOperation;
			}
			
			else {
				doubleRotateLeft(node);
				node.parent.rank++;
				node.rank--;
				numOfOperation+=2;
				return numOfOperation;
			}

		}
	  }
	  if (rankRightDiff == 3) {
		  if (rankLeftDiff == 2) {
				demote (node);
				numOfOperation++;
				return balanceTreeAfterDeletion(node.parent, numOfOperation);
			}
			else if (rankLeftDiff ==1) {
				WAVLNode y = node.left;
				int yRightDiff = y.rank - y.right.rank;
				int yLeftDiff = y.rank - y.left.rank;
				if (yRightDiff == 2 && yLeftDiff==2) {
					y.rank --;
					node.rank --;
					numOfOperation+=2;
					return balanceTreeAfterDeletion(node.parent, numOfOperation);
				}
				else if (yLeftDiff ==1) {
					rotateRight(node);
					node.parent.rank++;
					numOfOperation ++;
					if (isLeaf(node)) {
						rankLeftDiff= node.getRank() - node.left.getRank();
						rankRightDiff= node.getRank() - node.right.getRank(); 
						if (rankLeftDiff ==2 && rankRightDiff==2)  {
							 demote(node);
						 }
					
				}
					return numOfOperation;
				}
				else {
					doubleRotateRight(node);
					node.parent.rank++;
					node.rank--;
					
					numOfOperation+=2;
					return numOfOperation;
				}

			}  
	  }
	  
	  
		return numOfOperation;
	}

private void demote(WAVLNode node) {
	node.rank--;
	
}

private void doubleRotateLeft(WAVLNode node) {
	WAVLNode z = node;
	WAVLNode x = z.right;
	WAVLNode b = x.left;
	WAVLNode y = z.left;
	WAVLNode a = z.right.right;
	WAVLNode c = b.right;
	WAVLNode d = b.left;
	if (z.getKey()== root.getKey()) {
		root = b;	
	}
	WAVLNode zParrent = z.parent;
	x.setRank(x.getRank() - 1);
	z.setRank(z.getRank() -1);
	b.setRank(b.getRank() +1);
	b.setLeft(z);
	b.setRight(x);
	z.setRight(d);
	x.setLeft(c);
	c.setParent(x);
	d.setParent(z);
	x.setParent(b);
	z.setParent(b);
	b.setParent(zParrent);
	if (zParrent != null) {
		if (zParrent.getKey() > b.getKey()) {
			zParrent.setLeft(b);
		}
		else {
			zParrent.setRight(b);
		}
	}
		x.size = x.left.size+ x.right.size+1;
		z.size= z.left.size+ z.right.size+1;
		b.size=1+ b.left.size + b.right.size;	
	
}

private void rotateLeft(WAVLNode node) {
	WAVLNode z = node;
	WAVLNode y = z.left;
	WAVLNode x = z.right;
	WAVLNode b = x.left;
	WAVLNode zParent = z.parent;
	if (z.getKey()== root.getKey()) {
		root = x;	
	}
	z.setRank(z.getRank() -1);
	z.setRight(b);
	x.setLeft(z);
	b.setParent(z);
	z.setParent(x);
	x.setParent(zParent);
	if (zParent != null) {
		if (zParent.getKey() > x.getKey()) {
			zParent.setLeft(x);
		}
		else {
			zParent.setRight(x);
		}
	}
	z.size = 1+ z.left.size + z.right.size;
	x.size =  1+ x.left.size + x.right.size;

}

private void doubleRotateRight(WAVLNode node) {
	WAVLNode z = node;
	WAVLNode x = z.left;
	WAVLNode b = x.right;
	WAVLNode y = z.right;
	WAVLNode a = z.left.left;
	WAVLNode c = b.left;
	WAVLNode d = b.right;
	WAVLNode zParrent = z.parent;
	if (z.getKey()== root.getKey()) {
		root = b;	
	}
	x.setRank(x.getRank() - 1);
	z.setRank(z.getRank() -1);
	b.setRank(b.getRank() +1);
	b.setRight(z);
	b.setLeft(x);
	z.setLeft(d);
	x.setRight(c);
	c.setParent(x);
	d.setParent(z);
	x.setParent(b);
	z.setParent(b);
	b.setParent(zParrent);
	if (zParrent != null) {
		if (zParrent.getKey() > b.getKey()) {
			zParrent.setLeft(b);
		}
		else {
			zParrent.setRight(b);
		}
	}
	x.size = x.left.size+ x.right.size+1;
	z.size= z.left.size+ z.right.size+1;
	b.size=1+ b.left.size + b.right.size;
	
}

private void rotateRight(WAVLNode node) { 
	if (node.getKey()== root.getKey()) {
		root = node.left;	
	}
	node.setRank(node.getRank() -1);
	WAVLNode temp = node.left;
	node.setLeft(node.left.right);
	temp.setRight(temp.parent);
	temp.setParent(node.parent);
	if (node.getParent() != null) {
		if (node.getParent().getKey() > temp.getKey()) {
			node.parent.setLeft(temp);
		}
		else {
			node.parent.setRight(temp);
		}
	}
		node.setParent(temp);
		node.left.setParent(node);
		node.size = 1+ node.left.size + node.right.size;
		temp.size = 1+ temp.left.size + temp.right.size;
	
}
private void Promote(WAVLNode node) {
	node.setRank(node.getRank()+1);
}


public WAVLNode treePosition(WAVLNode insertionPoint, int k) {
	  WAVLNode copy=null;
	while( insertionPoint!= null && insertionPoint.getRank() != -1){
		copy=insertionPoint;
		if(k==insertionPoint.getKey()) {
			return insertionPoint;
		}
		else if(k< insertionPoint.getKey()) {
			insertionPoint=insertionPoint.left;
			
		}
		else {
			insertionPoint=insertionPoint.right;
		}
		
	}
	return copy;
}

/**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   if (root == null) {
		   return -1;
	   }
	   WAVLNode deletionPoint = treePosition(root,k);
	  if (deletionPoint.key != k) {
		return -1;  
	  }
	  else {
		  deletionPoint = findNodeForDeletion(deletionPoint);
	  }
	  if (deletionPoint.getKey() == root.getKey()) {
		  if (isLeaf(root)) {
		  root = null;
		  return 0;
		  }
		  else {
			  if (deletionPoint.right.isRealNode()) {
				  root = deletionPoint.right;
				  deletionPoint.right.parent = null;
			  }
			  else {
				  root = deletionPoint.left;
				  deletionPoint.left.parent = null;
			  }
			  return 0;
		  }
	  }
	  decreaseSize(deletionPoint);
	  if (isLeaf(deletionPoint)) {
		  if (deletionPoint.key == deletionPoint.parent.right.key) {
			  deletionPoint.parent.right = deletionPoint.right; 
			  deletionPoint.right.parent =deletionPoint.parent; 
		  }
		  else {
			  deletionPoint.parent.left = deletionPoint.left; 
			  deletionPoint.left.parent = deletionPoint.parent;
		  }
	  }
	  else {
		  if (deletionPoint.key == deletionPoint.parent.right.key) { 
			if (deletionPoint.right.isRealNode()) {
				deletionPoint.parent.right=deletionPoint.right; 
				deletionPoint.right.parent = deletionPoint.parent;
			}
			else {
				deletionPoint.parent.right=deletionPoint.left;
				deletionPoint.left.parent = deletionPoint.parent;
			}
		  }
		  else {
			  if (deletionPoint.right.isRealNode()) {
					deletionPoint.parent.left=deletionPoint.right; 
					deletionPoint.right.parent = deletionPoint.parent;
				}
				else {
					deletionPoint.parent.left=deletionPoint.left;
					deletionPoint.left.parent = deletionPoint.parent;
				}
		  }
	  }
	  
	   return balanceTreeAfterDeletion(deletionPoint.parent,0);	// to be replaced by student code
   }
   
   
   private void decreaseSize(WAVLNode deletionPoint) {
	   while (deletionPoint != null) {
		   deletionPoint.size --;
			deletionPoint = deletionPoint.parent;
		}
		
}

private WAVLNode findNodeForDeletion(WAVLNode deletionPoint) {
	   if (isLeaf(deletionPoint) || isUnary(deletionPoint)) {
		   return deletionPoint;
	   }
	   WAVLNode sucessor = sucessor(deletionPoint);
	   replace(deletionPoint,sucessor);
	  
	   
	   return sucessor;
}

private WAVLNode sucessor(WAVLNode deletionPoint) {
	WAVLNode y;
	if (deletionPoint.right.isRealNode()){
		return minWAVL(deletionPoint.right);
		}
	y=deletionPoint.parent;
	while(y != null && deletionPoint==y.right ) {
		deletionPoint=y;
		y=deletionPoint.parent;
	}
	
	return y;
}

private WAVLNode minWAVL(WAVLNode node) {
	while(node.left.isRealNode()) {
		node=node.left;
	}
	return node;
}

private void replace(WAVLNode deletionPoint, WAVLNode sucessor) {
int keyDeletion = deletionPoint.key;
String valueDeletion = deletionPoint.getValue();
deletionPoint.key = sucessor.key;
deletionPoint.info = sucessor.getValue();
sucessor.key = keyDeletion;
sucessor.info = valueDeletion;
	
}

private boolean isUnary(WAVLNode deletionPoint) {
	if((deletionPoint.left.isRealNode()) && (!deletionPoint.right.isRealNode())) {
		return true;
	}
	if((deletionPoint.right.isRealNode()) && (!deletionPoint.left.isRealNode())) {
		return true;
	}
	return false;
}

private boolean isLeaf(WAVLNode deletionPoint) {
	if((!deletionPoint.right.isRealNode()) &&(!deletionPoint.left.isRealNode())) {
		return true;
	}
	return false;
}

/**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   if (root == null) {
		   return null;
	   }
	   
	   return minWAVL(root).getValue(); // to be replaced by student code
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if (root == null) {
		   return null;
	   }
	   WAVLNode node = root;
	   while (node.right.isRealNode()) {
		  node = node.right; 
	   }
	   
	   return node.getValue(); // to be replaced by student code
   }


  private WAVLNode[] NodesToArray() {
	  if (root == null) {
		  WAVLNode[] arr = {};
		  return arr;
	  }
	    WAVLNode[] arr = new WAVLNode[root.size]; // to be replaced by student code
        WAVLNode node = minWAVL(root);
        arr[0] = node;
        int index = 1;
        node = sucessor(node);
        while (node != null) {
        	arr[index] = node;
            index ++;
            node = sucessor(node);
        }
        
        return arr; 
  }
  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
        WAVLNode [] arrNodes = NodesToArray();
        int[] arr = new int[arrNodes.length]; // to be replaced by student code
        for (int i=0; i<arrNodes.length; i++) {
        	arr[i] = arrNodes[i].getKey();
        }
       
        return arr;              // to be replaced by student code
  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  WAVLNode [] arrNodes = NodesToArray();
      String[] arr = new String[arrNodes.length]; // to be replaced by student code
      for (int i=0; i<arrNodes.length; i++) {
      	arr[i] = arrNodes[i].getValue();
      }
     
      return arr;              // to be replaced by student code
  }

   /** 
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   if (root == null) {
		   return 0;
	   }
	   return root.size; // to be replaced by student code
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root WAVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IWAVLNode getRoot()
   {
	   return this.root;
   }
     /**
    * public int select(int i)
    *
    * Returns the value of the i'th smallest key (return -1 if tree is empty)
    * Example 1: select(1) returns the value of the node with minimal key 
	* Example 2: select(size()) returns the value of the node with maximal key 
	* Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor 	
    *
	* precondition: size() >= i > 0
    * postcondition: none
    */   
   public String select(int i)
   {
	   if (root == null) {
		   return "-1";
	   }
	   String [] arr = infoToArray();
	   if (i > arr.length -1 || i<0) {
		   return "-1";
	   }
	   else {
		   return arr[i];
	   } 
   }

	/**
	   * public interface IWAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IWAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public IWAVLNode getLeft(); //returns left child (if there is no left child return null)
		public IWAVLNode getRight(); //returns right child (if there is no right child return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public int getSubtreeSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))
	}

   /**
   * public class WAVLNode
   *
   * If you wish to implement classes other than WAVLTree
   * (for example WAVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IWAVLNode)
   */
	public class WAVLNode implements IWAVLNode{
	  private int key;
	  private String info;
	  private int rank;
	  private WAVLNode parent;
	  private WAVLNode left;
	  private WAVLNode right;
	  private int size;
	  public WAVLNode () {
		  this.rank = -1;
		  this.key=-1;
	  }
	  public WAVLNode(int key, String info) {
		  this.key = key;
		  this.info = info;
		  this.size= 1;
		  this.left= new WAVLNode();
		  this.right= new WAVLNode();
		  this.left.setParent(this);
		  this.right.setParent(this);
	  }
		public int getKey()
		{
			return this.key; // to be replaced by student code
		}
		public String getValue()
		{
			return this.info; // to be replaced by student code
		}
		public IWAVLNode getLeft()
		{
			return this.left; // to be replaced by student code
		}
		public IWAVLNode getRight()
		{
			return this.right; // to be replaced by student code
		}
		// Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public boolean isRealNode(){
			if (this.rank == -1) {
				return false;
			}
			return true; // to be replaced by student code
				}

		public int getSubtreeSize(){
					return this.size; // to be replaced by student code
				}
			
		public int getRank() {
			return this.rank;
		}
				
		public WAVLNode getParent() {
					
			return this.parent;
				}
		private void setValue (String info) {
			this.info = info;
		}
		private void setRank (int r) {
			this.rank = r;
		}
		private void setLeft (int k, String i) {
			 WAVLNode left = new WAVLNode(k,i);
			 this.left = left;
		}
		private void setLeft (WAVLNode left) {
			this.left = left;
		}
		
		private void setRight (int k, String i) {
			WAVLNode right = new WAVLNode(k,i);
			 this.right = right;
		}
		private void setRight (WAVLNode right) {
			this.right = right;
		}
		private void setParent (int k, String i) {
			WAVLNode parent = new WAVLNode(k,i);
			this.parent = parent;
		}
		private void setParent (WAVLNode parent) {
			this.parent = parent;
		}
		
  }

}
  

