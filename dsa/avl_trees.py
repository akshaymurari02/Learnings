

class Node:

    def __init__(self, val):
        self.val=val
        self.left=None
        self.right=None
        self.height=1
        self.count=1

class TreePrinter:

    def __init__(self):
        pass

    def print_inorder(self,root):
        if root is None:
            return
        self.print_inorder(root.left)
        if root.count > 1:
            print(f"{root.val}(x{root.count})", end="  ")
        else:
            print(root.val, end="  ")
        self.print_inorder(root.right)

    def print_tree(self, root, level=0, prefix="Root: "):
        if root is not None:
            bf = (root.left.height if root.left else 0) - (root.right.height if root.right else 0)
            print(" " * (level * 4) + prefix + str(root.val) + f" [bf={bf}]")
            if root.left or root.right:
                if root.left:
                    self.print_tree(root.left, level + 1, "L--- ")
                else:
                    print(" " * ((level + 1) * 4) + "L--- (empty)")
                if root.right:
                    self.print_tree(root.right, level + 1, "R--- ")
                else:
                    print(" " * ((level + 1) * 4) + "R--- (empty)")

class AVLTree:
    
    def __init__(self):
        self.root=None
        self.printer=TreePrinter()

    def get_height(self,root):
        if root==None:
            return 0
        return root.height

    def get_balance(self,root):
        if root==None:
            return 0
        return self.get_height(root.left) - self.get_height(root.right)

    def update_height(self,root):
        root.height = 1 + max(self.get_height(root.left), self.get_height(root.right))

    def insert_into(self,root,val):
        if root is None:
            return Node(val)
        
        if val < root.val:
            root.left=self.insert_into(root.left,val)
        elif val > root.val:
            root.right=self.insert_into(root.right,val)
        else:
            root.count += 1
            return root

        self.update_height(root)

        balance = self.get_balance(root)

        if balance > 1 and val < root.left.val:
            return self.right_rotate(root)

        if balance < -1 and val > root.right.val:
            return self.left_rotate(root)

        if balance > 1 and val > root.left.val:
            root.left = self.left_rotate(root.left)
            return self.right_rotate(root)

        if balance < -1 and val < root.right.val:
            root.right = self.right_rotate(root.right)
            return self.left_rotate(root)

        return root
    
    def insert(self,val):
        self.root=self.insert_into(self.root,val)

    def right_rotate(self,root):
        new_root=root.left
        root.left=new_root.right
        new_root.right=root
        self.update_height(root)
        self.update_height(new_root)
        return new_root

    def left_rotate(self,root):
        new_root=root.right
        root.right=new_root.left
        new_root.left=root
        self.update_height(root)
        self.update_height(new_root)
        return new_root

    def print(self):
        self.printer.print_inorder(self.root)
        print()

    def print_tree(self):
        self.printer.print_tree(self.root)

tree=AVLTree()

values=[1,2,3,-1,4,-2,5,3,3,1]

print("Inserting:", values)
print("=" * 40)

for val in values:
    tree.insert(val)
    print(f"\nAfter inserting {val}:")
    tree.print_tree()

print("\n" + "=" * 40)
print("In-order (should be sorted):")
tree.print()