import openpyxl

wb = openpyxl.Workbook()
# remove default
wb.remove(wb.active)

def add_sheet(title, rows):
    ws = wb.create_sheet(title=title[:31])
    ws.append(["Subtopic","Problem","Link","Difficulty","Asked By","Status (✅/❌)"])
    for r in rows:
        ws.append(r)

# 1. Arrays & Hashing (15)
arrays = [
    ("Hash Map","Two Sum","https://leetcode.com/problems/two-sum/","Easy","Google, Amazon",""),
    ("Hash Set","Contains Duplicate","https://leetcode.com/problems/contains-duplicate/","Easy","Amazon, Microsoft",""),
    ("Counting","Valid Anagram","https://leetcode.com/problems/valid-anagram/","Easy","Google, Microsoft",""),
    ("Hash Map Grouping","Group Anagrams","https://leetcode.com/problems/group-anagrams/","Medium","Google, Microsoft",""),
    ("Top-K + Heap","Top K Frequent Elements","https://leetcode.com/problems/top-k-frequent-elements/","Medium","Amazon, Google",""),
    ("Matrix Validation","Valid Sudoku","https://leetcode.com/problems/valid-sudoku/","Medium","Google",""),
    ("Hash Set","Longest Consecutive Sequence","https://leetcode.com/problems/longest-consecutive-sequence/","Medium","Google, Facebook",""),
    ("Prefix/Suffix","Product of Array Except Self","https://leetcode.com/problems/product-of-array-except-self/","Medium","Amazon, Microsoft",""),
    ("Prefix Sum","Subarray Sum Equals K","https://leetcode.com/problems/subarray-sum-equals-k/","Medium","Google, Amazon",""),
    ("Greedy/Single Pass","Best Time to Buy and Sell Stock","https://leetcode.com/problems/best-time-to-buy-and-sell-stock/","Easy","Amazon, Microsoft",""),
    ("Majority","Majority Element","https://leetcode.com/problems/majority-element/","Easy","Google",""),
    ("Counting","Ransom Note","https://leetcode.com/problems/ransom-note/","Easy","Amazon",""),
    ("Hash Map","Isomorphic Strings","https://leetcode.com/problems/isomorphic-strings/","Easy","Microsoft",""),
    ("Counting","Find All Anagrams in a String","https://leetcode.com/problems/find-all-anagrams-in-a-string/","Medium","Amazon, Microsoft",""),
    ("Design","Design HashMap","https://leetcode.com/problems/design-hashmap/","Easy","Microsoft",""),
]
add_sheet("Arrays & Hashing", arrays)

# 2. Two Pointers (15)
two_pointers = [
    ("Opposite Dir","Valid Palindrome","https://leetcode.com/problems/valid-palindrome/","Easy","Amazon, Microsoft",""),
    ("Opposite Dir","Two Sum II - Input array is sorted","https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/","Medium","Google, Amazon",""),
    ("Fix One + 2P","3Sum","https://leetcode.com/problems/3sum/","Medium","Google, Microsoft",""),
    ("Fix One + 2P","3Sum Closest","https://leetcode.com/problems/3sum-closest/","Medium","Amazon",""),
    ("K-Sum","4Sum","https://leetcode.com/problems/4sum/","Medium","Google",""),
    ("Opposite Dir","Container With Most Water","https://leetcode.com/problems/container-with-most-water/","Medium","Amazon, Apple",""),
    ("Two Pointers","Trapping Rain Water","https://leetcode.com/problems/trapping-rain-water/","Hard","Amazon, Google",""),
    ("Shrink/Expand","Remove Duplicates from Sorted Array","https://leetcode.com/problems/remove-duplicates-from-sorted-array/","Easy","Amazon",""),
    ("Shrink/Expand","Remove Element","https://leetcode.com/problems/remove-element/","Easy","Microsoft",""),
    ("Ends Meet","Squares of a Sorted Array","https://leetcode.com/problems/squares-of-a-sorted-array/","Easy","Google",""),
    ("Fast/Slow","Remove Nth Node From End of List","https://leetcode.com/problems/remove-nth-node-from-end-of-list/","Medium","Amazon, Microsoft",""),
    ("Fast/Slow","Middle of the Linked List","https://leetcode.com/problems/middle-of-the-linked-list/","Easy","Google",""),
    ("Expand Center","Palindromic Substrings","https://leetcode.com/problems/palindromic-substrings/","Medium","Amazon",""),
    ("Cleanup","Backspace String Compare","https://leetcode.com/problems/backspace-string-compare/","Easy","Microsoft",""),
    ("Partition","Sort Array By Parity","https://leetcode.com/problems/sort-array-by-parity/","Easy","Amazon",""),
]
add_sheet("Two Pointers", two_pointers)

# 3. Sliding Window (15)
sliding_window = [
    ("Variable Window","Longest Substring Without Repeating Characters","https://leetcode.com/problems/longest-substring-without-repeating-characters/","Medium","Microsoft, Amazon",""),
    ("Variable Window","Minimum Window Substring","https://leetcode.com/problems/minimum-window-substring/","Hard","Google, Bloomberg",""),
    ("Fixed/Max-Freq","Longest Repeating Character Replacement","https://leetcode.com/problems/longest-repeating-character-replacement/","Medium","Amazon",""),
    ("Permutation","Permutation in String","https://leetcode.com/problems/permutation-in-string/","Medium","Google",""),
    ("Fixed + Deque","Sliding Window Maximum","https://leetcode.com/problems/sliding-window-maximum/","Hard","Amazon, Google",""),
    ("Counting","Find All Anagrams in a String","https://leetcode.com/problems/find-all-anagrams-in-a-string/","Medium","Amazon",""),
    ("Variable Window","Fruit Into Baskets","https://leetcode.com/problems/fruit-into-baskets/","Medium","Google",""),
    ("Variable Window","Max Consecutive Ones III","https://leetcode.com/problems/max-consecutive-ones-iii/","Medium","Amazon",""),
    ("Fixed Window","Maximum Average Subarray I","https://leetcode.com/problems/maximum-average-subarray-i/","Easy","Microsoft",""),
    ("Product Window","Subarray Product Less Than K","https://leetcode.com/problems/subarray-product-less-than-k/","Medium","Amazon",""),
    ("Score Window","Maximum Erasure Value","https://leetcode.com/problems/maximum-erasure-value/","Medium","Google",""),
    ("Sum Window","Minimum Size Subarray Sum","https://leetcode.com/problems/minimum-size-subarray-sum/","Medium","Amazon",""),
    ("Budget Window","Get Equal Substrings Within Budget","https://leetcode.com/problems/get-equal-substrings-within-budget/","Medium","Google",""),
    ("Vowel Window","Maximum Number of Vowels in a Substring of Given Length","https://leetcode.com/problems/maximum-number-of-vowels-in-a-substring-of-given-length/","Medium","Microsoft",""),
    ("Count Distinct","Length of Longest Substring with At Most K Distinct Characters","https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/","Medium","Amazon",""),
]
add_sheet("Sliding Window", sliding_window)

# 4. Prefix Sums (15)
prefix_sums = [
    ("1D Prefix","Subarray Sum Equals K","https://leetcode.com/problems/subarray-sum-equals-k/","Medium","Google, Amazon",""),
    ("Modulo Prefix","Continuous Subarray Sum","https://leetcode.com/problems/continuous-subarray-sum/","Medium","Microsoft",""),
    ("Modulo Prefix","Subarray Sums Divisible by K","https://leetcode.com/problems/subarray-sums-divisible-by-k/","Medium","Google",""),
    ("Balance Prefix","Contiguous Array","https://leetcode.com/problems/contiguous-array/","Medium","Facebook",""),
    ("Count Prefix","Number of Sub-arrays of Size K and Average >= Threshold","https://leetcode.com/problems/number-of-sub-arrays-of-size-k-and-average-greater-than-or-equal-to-threshold/","Easy","Amazon",""),
    ("1D Prefix","Find Pivot Index","https://leetcode.com/problems/find-pivot-index/","Easy","Microsoft",""),
    ("2D Prefix","Range Sum Query 2D - Immutable","https://leetcode.com/problems/range-sum-query-2d-immutable/","Medium","Google",""),
    ("Shortest >=K","Shortest Subarray with Sum at Least K","https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/","Hard","Google",""),
    ("Max <=K (2D)","Max Sum of Rectangle No Larger Than K","https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/","Hard","Google",""),
    ("Parity Prefix","Number of Sub-arrays With Odd Sum","https://leetcode.com/problems/number-of-sub-arrays-with-odd-sum/","Easy","Amazon",""),
    ("Count Prefix","Binary Subarrays With Sum","https://leetcode.com/problems/binary-subarrays-with-sum/","Medium","Google",""),
    ("Nice Count","Count Number of Nice Subarrays","https://leetcode.com/problems/count-number-of-nice-subarrays/","Medium","Facebook",""),
    ("Alt Diff","Minimum Value to Get Positive Step by Step Sum","https://leetcode.com/problems/minimum-value-to-get-positive-step-by-step-sum/","Easy","Google",""),
    ("Balance","Find the Longest Substring Containing Vowels in Even Counts","https://leetcode.com/problems/find-the-longest-substring-containing-vowels-in-even-counts/","Medium","Google",""),
    ("Diff Array","Corporate Flight Bookings","https://leetcode.com/problems/corporate-flight-bookings/","Medium","Amazon",""),
]
add_sheet("Prefix Sums", prefix_sums)

# 5. Stack (15)
stack_rows = [
    ("Basic Stack","Valid Parentheses","https://leetcode.com/problems/valid-parentheses/","Easy","Amazon, Microsoft",""),
    ("Min Stack","Min Stack","https://leetcode.com/problems/min-stack/","Medium","Amazon",""),
    ("Eval","Evaluate Reverse Polish Notation","https://leetcode.com/problems/evaluate-reverse-polish-notation/","Medium","Google",""),
    ("Decoding","Decode String","https://leetcode.com/problems/decode-string/","Medium","Amazon",""),
    ("Monotonic","Daily Temperatures","https://leetcode.com/problems/daily-temperatures/","Medium","Microsoft",""),
    ("Greedy+Stack","Remove K Digits","https://leetcode.com/problems/remove-k-digits/","Medium","Google",""),
    ("Monotonic","Largest Rectangle in Histogram","https://leetcode.com/problems/largest-rectangle-in-histogram/","Hard","Google, Facebook",""),
    ("Parentheses","Longest Valid Parentheses","https://leetcode.com/problems/longest-valid-parentheses/","Hard","Google",""),
    ("Path","Simplify Path","https://leetcode.com/problems/simplify-path/","Medium","Amazon",""),
    ("Collision","Asteroid Collision","https://leetcode.com/problems/asteroid-collision/","Medium","Amazon",""),
    ("Dedup","Remove Duplicate Letters","https://leetcode.com/problems/remove-duplicate-letters/","Medium","Google",""),
    ("Dedup k","Remove All Adjacent Duplicates in String II","https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/","Medium","Amazon",""),
    ("Calc","Basic Calculator","https://leetcode.com/problems/basic-calculator/","Hard","Google",""),
    ("Calc","Basic Calculator II","https://leetcode.com/problems/basic-calculator-ii/","Medium","Microsoft",""),
    ("Water","Trapping Rain Water (Stack)","https://leetcode.com/problems/trapping-rain-water/","Hard","Amazon",""),
]
add_sheet("Stack", stack_rows)

# 6. Binary Search (15)
binary_rows = [
    ("Classic","Binary Search","https://leetcode.com/problems/binary-search/","Easy","Microsoft",""),
    ("Boundaries","Search Insert Position","https://leetcode.com/problems/search-insert-position/","Easy","Amazon",""),
    ("Boundaries","Find First and Last Position of Element","https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/","Medium","Google",""),
    ("Modified","Search in Rotated Sorted Array","https://leetcode.com/problems/search-in-rotated-sorted-array/","Medium","Microsoft",""),
    ("Modified","Search in Rotated Sorted Array II","https://leetcode.com/problems/search-in-rotated-sorted-array-ii/","Medium","Amazon",""),
    ("Min in Rotated","Find Minimum in Rotated Sorted Array","https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/","Medium","Google",""),
    ("Min in Rotated","Find Minimum in Rotated Sorted Array II","https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/","Hard","Google",""),
    ("Peak","Find Peak Element","https://leetcode.com/problems/find-peak-element/","Medium","Amazon",""),
    ("On Answer","Koko Eating Bananas","https://leetcode.com/problems/koko-eating-bananas/","Medium","Google",""),
    ("On Answer","Capacity To Ship Packages Within D Days","https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/","Medium","Amazon",""),
    ("On Answer","Split Array Largest Sum","https://leetcode.com/problems/split-array-largest-sum/","Hard","Amazon, Google",""),
    ("Application","Median of Two Sorted Arrays","https://leetcode.com/problems/median-of-two-sorted-arrays/","Hard","Google",""),
    ("First Bad","First Bad Version","https://leetcode.com/problems/first-bad-version/","Easy","Facebook",""),
    ("Matrix","Search a 2D Matrix","https://leetcode.com/problems/search-a-2d-matrix/","Medium","Google",""),
    ("Bounds","Heaters","https://leetcode.com/problems/heaters/","Medium","Amazon",""),
]
add_sheet("Binary Search", binary_rows)

# 7. Linked List (15)
ll_rows = [
    ("Basics","Reverse Linked List","https://leetcode.com/problems/reverse-linked-list/","Easy","Amazon, Microsoft",""),
    ("Basics","Merge Two Sorted Lists","https://leetcode.com/problems/merge-two-sorted-lists/","Easy","Google",""),
    ("Two Pointers","Linked List Cycle","https://leetcode.com/problems/linked-list-cycle/","Easy","Amazon",""),
    ("Two Pointers","Linked List Cycle II","https://leetcode.com/problems/linked-list-cycle-ii/","Medium","Amazon",""),
    ("Two Pointers","Intersection of Two Linked Lists","https://leetcode.com/problems/intersection-of-two-linked-lists/","Easy","Microsoft",""),
    ("Two Pointers","Remove Nth Node From End of List","https://leetcode.com/problems/remove-nth-node-from-end-of-list/","Medium","Amazon",""),
    ("Palindrome","Palindrome Linked List","https://leetcode.com/problems/palindrome-linked-list/","Easy","Google",""),
    ("Math","Add Two Numbers","https://leetcode.com/problems/add-two-numbers/","Medium","Amazon",""),
    ("Copy","Copy List with Random Pointer","https://leetcode.com/problems/copy-list-with-random-pointer/","Medium","Amazon, Google",""),
    ("Reverse","Reverse Linked List II","https://leetcode.com/problems/reverse-linked-list-ii/","Medium","Microsoft",""),
    ("Rotate","Rotate List","https://leetcode.com/problems/rotate-list/","Medium","Microsoft",""),
    ("Swap","Swap Nodes in Pairs","https://leetcode.com/problems/swap-nodes-in-pairs/","Medium","Amazon",""),
    ("K-Group","Reverse Nodes in k-Group","https://leetcode.com/problems/reverse-nodes-in-k-group/","Hard","Google",""),
    ("Math","Add Two Numbers II","https://leetcode.com/problems/add-two-numbers-ii/","Medium","Facebook",""),
    ("Design","LRU Cache","https://leetcode.com/problems/lru-cache/","Medium","Amazon, Microsoft",""),
]
add_sheet("Linked List", ll_rows)

# 8. Trees (15)
trees_rows = [
    ("DFS","Maximum Depth of Binary Tree","https://leetcode.com/problems/maximum-depth-of-binary-tree/","Easy","Amazon",""),
    ("DFS","Diameter of Binary Tree","https://leetcode.com/problems/diameter-of-binary-tree/","Easy","Google",""),
    ("DFS","Balanced Binary Tree","https://leetcode.com/problems/balanced-binary-tree/","Easy","Microsoft",""),
    ("Compare","Same Tree","https://leetcode.com/problems/same-tree/","Easy","Amazon",""),
    ("Subtree","Subtree of Another Tree","https://leetcode.com/problems/subtree-of-another-tree/","Easy","Amazon",""),
    ("BST","Lowest Common Ancestor of a BST","https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/","Medium","Google",""),
    ("BFS","Binary Tree Level Order Traversal","https://leetcode.com/problems/binary-tree-level-order-traversal/","Medium","Amazon",""),
    ("View","Binary Tree Right Side View","https://leetcode.com/problems/binary-tree-right-side-view/","Medium","Microsoft",""),
    ("Path Info","Count Good Nodes in Binary Tree","https://leetcode.com/problems/count-good-nodes-in-binary-tree/","Medium","Google",""),
    ("Validate","Validate Binary Search Tree","https://leetcode.com/problems/validate-binary-search-tree/","Medium","Google",""),
    ("Order Stat","Kth Smallest Element in a BST","https://leetcode.com/problems/kth-smallest-element-in-a-bst/","Medium","Amazon",""),
    ("Build","Construct Binary Tree from Preorder and Inorder Traversal","https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/","Medium","Google",""),
    ("Hard DFS","Binary Tree Maximum Path Sum","https://leetcode.com/problems/binary-tree-maximum-path-sum/","Hard","Google, Microsoft",""),
    ("Serialize","Serialize and Deserialize Binary Tree","https://leetcode.com/problems/serialize-and-deserialize-binary-tree/","Hard","Google",""),
    ("LCA","Lowest Common Ancestor of a Binary Tree","https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/","Medium","Amazon",""),
]
add_sheet("Trees", trees_rows)

# 9. Tries (15)
tries_rows = [
    ("Implement","Implement Trie (Prefix Tree)","https://leetcode.com/problems/implement-trie-prefix-tree/","Medium","Google",""),
    ("Wildcard","Design Add and Search Words Data Structure","https://leetcode.com/problems/design-add-and-search-words-data-structure/","Medium","Facebook",""),
    ("Board Search","Word Search II","https://leetcode.com/problems/word-search-ii/","Hard","Google",""),
    ("Replace","Replace Words","https://leetcode.com/problems/replace-words/","Medium","Google",""),
    ("Dictionary","Longest Word in Dictionary","https://leetcode.com/problems/longest-word-in-dictionary/","Medium","Amazon",""),
    ("Encoding","Short Encoding of Words","https://leetcode.com/problems/short-encoding-of-words/","Medium","Google",""),
    ("Suggestions","Search Suggestions System","https://leetcode.com/problems/search-suggestions-system/","Medium","Amazon",""),
    ("Sum","Map Sum Pairs","https://leetcode.com/problems/map-sum-pairs/","Medium","Google",""),
    ("Magic Dict","Implement Magic Dictionary","https://leetcode.com/problems/implement-magic-dictionary/","Medium","Facebook",""),
    ("Stream","Stream of Characters","https://leetcode.com/problems/stream-of-characters/","Hard","Google",""),
    ("XOR Trie","Maximum XOR of Two Numbers in an Array","https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/","Medium","Google",""),
    ("Concat","Concatenated Words","https://leetcode.com/problems/concatenated-words/","Hard","Amazon",""),
    ("Phone","Letter Combinations of a Phone Number","https://leetcode.com/problems/letter-combinations-of-a-phone-number/","Medium","Google",""),
    ("Pairs","Maximum XOR With an Element From Array","https://leetcode.com/problems/maximum-xor-with-an-element-from-array/","Hard","Google",""),
    ("Count Prefix","Count Prefixes of a Given String","https://leetcode.com/problems/count-prefixes-of-a-given-string/","Easy","—",""),
]
add_sheet("Tries", tries_rows)

# 10. Heap / Priority Queue (15)
heap_rows = [
    ("Kth","Kth Largest Element in an Array","https://leetcode.com/problems/kth-largest-element-in-an-array/","Medium","Amazon",""),
    ("Top-K","Top K Frequent Elements","https://leetcode.com/problems/top-k-frequent-elements/","Medium","Amazon, Google",""),
    ("Geometry","K Closest Points to Origin","https://leetcode.com/problems/k-closest-points-to-origin/","Medium","Google",""),
    ("Matrix","Kth Smallest Element in a Sorted Matrix","https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/","Medium","Amazon",""),
    ("Stream","Kth Largest Element in a Stream","https://leetcode.com/problems/kth-largest-element-in-a-stream/","Easy","—",""),
    ("Stream Median","Find Median from Data Stream","https://leetcode.com/problems/find-median-from-data-stream/","Hard","Google",""),
    ("Window Median","Sliding Window Median","https://leetcode.com/problems/sliding-window-median/","Hard","Google",""),
    ("Greedy+Heap","IPO","https://leetcode.com/problems/ipo/","Hard","Amazon",""),
    ("Merge","Merge k Sorted Lists","https://leetcode.com/problems/merge-k-sorted-lists/","Hard","Amazon, Google",""),
    ("Words","Top K Frequent Words","https://leetcode.com/problems/top-k-frequent-words/","Medium","Amazon",""),
    ("Greedy","Task Scheduler","https://leetcode.com/problems/task-scheduler/","Medium","Amazon, Microsoft",""),
    ("Stones","Last Stone Weight","https://leetcode.com/problems/last-stone-weight/","Easy","—",""),
    ("Workers","Minimum Cost to Hire K Workers","https://leetcode.com/problems/minimum-cost-to-hire-k-workers/","Hard","Google",""),
    ("Ropes","Minimum Cost to Connect Sticks","https://leetcode.com/problems/minimum-cost-to-connect-sticks/","Medium","Amazon",""),
    ("Schedule","Single-Threaded CPU","https://leetcode.com/problems/single-threaded-cpu/","Medium","Google",""),
]
add_sheet("Heap-Priority Queue", heap_rows)

# 11. Backtracking (15)
bt_rows = [
    ("Permute","Permutations","https://leetcode.com/problems/permutations/","Medium","Amazon",""),
    ("Permute","Permutations II","https://leetcode.com/problems/permutations-ii/","Medium","Amazon",""),
    ("Combine","Combinations","https://leetcode.com/problems/combinations/","Medium","Microsoft",""),
    ("Subsets","Subsets","https://leetcode.com/problems/subsets/","Medium","Amazon",""),
    ("Subsets","Subsets II","https://leetcode.com/problems/subsets-ii/","Medium","Amazon",""),
    ("Pick/Target","Combination Sum","https://leetcode.com/problems/combination-sum/","Medium","Amazon",""),
    ("Pick/Target","Combination Sum II","https://leetcode.com/problems/combination-sum-ii/","Medium","Amazon",""),
    ("Partition","Palindrome Partitioning","https://leetcode.com/problems/palindrome-partitioning/","Medium","Google",""),
    ("Grid","Word Search","https://leetcode.com/problems/word-search/","Medium","Amazon",""),
    ("Board","Sudoku Solver","https://leetcode.com/problems/sudoku-solver/","Hard","Google",""),
    ("N-Queens","N-Queens","https://leetcode.com/problems/n-queens/","Hard","Amazon",""),
    ("Cases","Letter Case Permutation","https://leetcode.com/problems/letter-case-permutation/","Medium","Facebook",""),
    ("IP","Restore IP Addresses","https://leetcode.com/problems/restore-ip-addresses/","Medium","Amazon",""),
    ("Split","Partition to K Equal Sum Subsets","https://leetcode.com/problems/partition-to-k-equal-sum-subsets/","Medium","Amazon",""),
    ("Phone","Letter Combinations of a Phone Number","https://leetcode.com/problems/letter-combinations-of-a-phone-number/","Medium","Google",""),
]
add_sheet("Backtracking", bt_rows)

# 12. Graphs (BFS/DFS/UF) (15)
graphs_rows = [
    ("Grid BFS/DFS","Number of Islands","https://leetcode.com/problems/number-of-islands/","Medium","Google",""),
    ("Clone","Clone Graph","https://leetcode.com/problems/clone-graph/","Medium","Facebook",""),
    ("Grid DFS","Max Area of Island","https://leetcode.com/problems/max-area-of-island/","Medium","Amazon",""),
    ("Multi-source","Pacific Atlantic Water Flow","https://leetcode.com/problems/pacific-atlantic-water-flow/","Medium","Google",""),
    ("Boundary","Surrounded Regions","https://leetcode.com/problems/surrounded-regions/","Medium","Facebook",""),
    ("Multi-source BFS","Rotting Oranges","https://leetcode.com/problems/rotting-oranges/","Medium","Amazon",""),
    ("Topo","Course Schedule","https://leetcode.com/problems/course-schedule/","Medium","Google",""),
    ("Topo","Course Schedule II","https://leetcode.com/problems/course-schedule-ii/","Medium","Google",""),
    ("Union Find","Redundant Connection","https://leetcode.com/problems/redundant-connection/","Medium","Google",""),
    ("Union Find","Accounts Merge","https://leetcode.com/problems/accounts-merge/","Medium","Facebook",""),
    ("Union Find","Number of Provinces","https://leetcode.com/problems/number-of-provinces/","Medium","Google",""),
    ("Bipartite","Is Graph Bipartite?","https://leetcode.com/problems/is-graph-bipartite/","Medium","Google",""),
    ("Shortest Path","Word Ladder","https://leetcode.com/problems/word-ladder/","Hard","Amazon",""),
    ("Math BFS","Open the Lock","https://leetcode.com/problems/open-the-lock/","Medium","Google",""),
    ("Evaluate","Evaluate Division","https://leetcode.com/problems/evaluate-division/","Medium","Google",""),
]
add_sheet("Graphs (BFS-DFS-Union Find)", graphs_rows)

# 13. Greedy (15)
greedy_rows = [
    ("Intervals","Non-overlapping Intervals","https://leetcode.com/problems/non-overlapping-intervals/","Medium","Google",""),
    ("Partition","Partition Labels","https://leetcode.com/problems/partition-labels/","Medium","Amazon",""),
    ("Gas","Gas Station","https://leetcode.com/problems/gas-station/","Medium","Google",""),
    ("Sort Greedy","Queue Reconstruction by Height","https://leetcode.com/problems/queue-reconstruction-by-height/","Medium","Google",""),
    ("Scheduling","Task Scheduler","https://leetcode.com/problems/task-scheduler/","Medium","Amazon",""),
    ("Stock","Best Time to Buy and Sell Stock II","https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/","Medium","Amazon",""),
    ("Flowers","Can Place Flowers","https://leetcode.com/problems/can-place-flowers/","Easy","Amazon",""),
    ("Arrows","Minimum Number of Arrows to Burst Balloons","https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/","Medium","Amazon",""),
    ("Negations","Maximize Sum Of Array After K Negations","https://leetcode.com/problems/maximize-sum-of-array-after-k-negations/","Easy","—",""),
    ("Boats","Boats to Save People","https://leetcode.com/problems/boats-to-save-people/","Medium","Google",""),
    ("City","Two City Scheduling","https://leetcode.com/problems/two-city-scheduling/","Medium","Google",""),
    ("Candy","Candy","https://leetcode.com/problems/candy/","Hard","Amazon",""),
    ("Meetings","Maximum Units on a Truck","https://leetcode.com/problems/maximum-units-on-a-truck/","Easy","Amazon",""),
    ("Digits","Remove K Digits","https://leetcode.com/problems/remove-k-digits/","Medium","Google",""),
    ("Median","Minimum Number of Swaps to Make the String Balanced","https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-string-balanced/","Medium","Amazon",""),
]
add_sheet("Greedy", greedy_rows)

# 14. Intervals (15)
interval_rows = [
    ("Merge","Merge Intervals","https://leetcode.com/problems/merge-intervals/","Medium","Google",""),
    ("Insert","Insert Interval","https://leetcode.com/problems/insert-interval/","Medium","Facebook",""),
    ("Erase","Non-overlapping Intervals","https://leetcode.com/problems/non-overlapping-intervals/","Medium","Google",""),
    ("Meeting","Meeting Rooms II","https://leetcode.com/problems/meeting-rooms-ii/","Medium","Google",""),
    ("Meeting","Meeting Rooms","https://leetcode.com/problems/meeting-rooms/","Easy","Google",""),
    ("Intersect","Interval List Intersections","https://leetcode.com/problems/interval-list-intersections/","Medium","Facebook",""),
    ("Covered","Remove Covered Intervals","https://leetcode.com/problems/remove-covered-intervals/","Medium","Amazon",""),
    ("Arrows","Min Arrows to Burst Balloons","https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/","Medium","Amazon",""),
    ("Calendar","My Calendar I","https://leetcode.com/problems/my-calendar-i/","Medium","Google",""),
    ("Calendar","My Calendar II","https://leetcode.com/problems/my-calendar-ii/","Medium","Google",""),
    ("Calendar","My Calendar III","https://leetcode.com/problems/my-calendar-iii/","Hard","Google",""),
    ("Range","Range Module","https://leetcode.com/problems/range-module/","Hard","Google",""),
    ("Employee","Employee Free Time","https://leetcode.com/problems/employee-free-time/","Hard","—",""),
    ("Rooms","Minimum Number of Meeting Rooms","https://leetcode.com/problems/meeting-rooms-ii/","Medium","Google",""),
    ("Project","Car Pooling","https://leetcode.com/problems/car-pooling/","Medium","Amazon",""),
]
add_sheet("Intervals", interval_rows)

# 15. Recursion (15)
rec_rows = [
    ("Divide & Conquer","Pow(x, n)","https://leetcode.com/problems/powx-n/","Medium","Microsoft",""),
    ("Divide & Conquer","K-th Symbol in Grammar","https://leetcode.com/problems/k-th-symbol-in-grammar/","Medium","Google",""),
    ("Decode","Decode String","https://leetcode.com/problems/decode-string/","Medium","Amazon",""),
    ("Tree Recursion","Unique Binary Search Trees","https://leetcode.com/problems/unique-binary-search-trees/","Medium","Google",""),
    ("Tree Recursion","Different Ways to Add Parentheses","https://leetcode.com/problems/different-ways-to-add-parentheses/","Medium","Amazon",""),
    ("Backtrack","N-Queens","https://leetcode.com/problems/n-queens/","Hard","Amazon",""),
    ("Backtrack","Word Search","https://leetcode.com/problems/word-search/","Medium","Amazon",""),
    ("DFS","Target Sum","https://leetcode.com/problems/target-sum/","Medium","Facebook",""),
    ("DFS","Flood Fill","https://leetcode.com/problems/flood-fill/","Easy","Microsoft",""),
    ("Math Rec","Integer Break","https://leetcode.com/problems/integer-break/","Medium","Google",""),
    ("Recursion","Gray Code","https://leetcode.com/problems/gray-code/","Medium","Google",""),
    ("Recursion","Permutation Sequence","https://leetcode.com/problems/permutation-sequence/","Hard","Facebook",""),
    ("Tree Recursion","Validate Binary Search Tree","https://leetcode.com/problems/validate-binary-search-tree/","Medium","Google",""),
    ("Divide & Conquer","Closest Binary Search Tree Value II","https://leetcode.com/problems/closest-binary-search-tree-value-ii/","Hard","—",""),
    ("Divide & Conquer","Count of Smaller Numbers After Self","https://leetcode.com/problems/count-of-smaller-numbers-after-self/","Hard","Google",""),
]
add_sheet("Recursion", rec_rows)

# 16. Bit Manipulation (15)
bit_rows = [
    ("Bits","Single Number","https://leetcode.com/problems/single-number/","Easy","Google",""),
    ("Bits","Single Number II","https://leetcode.com/problems/single-number-ii/","Medium","Google",""),
    ("Bits","Single Number III","https://leetcode.com/problems/single-number-iii/","Medium","Facebook",""),
    ("Bits","Number of 1 Bits","https://leetcode.com/problems/number-of-1-bits/","Easy","Microsoft",""),
    ("Bits","Power of Two","https://leetcode.com/problems/power-of-two/","Easy","Google",""),
    ("Bits","Reverse Bits","https://leetcode.com/problems/reverse-bits/","Easy","Facebook",""),
    ("Bits","Missing Number","https://leetcode.com/problems/missing-number/","Easy","Google",""),
    ("Bits","Sum of Two Integers","https://leetcode.com/problems/sum-of-two-integers/","Medium","Google",""),
    ("Bits","Bitwise AND of Numbers Range","https://leetcode.com/problems/bitwise-and-of-numbers-range/","Medium","Facebook",""),
    ("DP+Bits","Counting Bits","https://leetcode.com/problems/counting-bits/","Easy","Microsoft",""),
    ("Bits","Find the Difference","https://leetcode.com/problems/find-the-difference/","Easy","—",""),
    ("Bits","Maximum XOR of Two Numbers","https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/","Medium","Google",""),
    ("Binary Search","Single Element in a Sorted Array","https://leetcode.com/problems/single-element-in-a-sorted-array/","Medium","Amazon",""),
    ("Bits","Partitioning Into Minimum Number Of Deci-Binary Numbers","https://leetcode.com/problems/partitioning-into-minimum-number-of-deci-binary-numbers/","Medium","—",""),
    ("Bits","Total Hamming Distance","https://leetcode.com/problems/total-hamming-distance/","Medium","Google",""),
]
add_sheet("Bit Manipulation", bit_rows)

# 17. Dynamic Programming (15)
dp_rows = [
    ("1D DP","Climbing Stairs","https://leetcode.com/problems/climbing-stairs/","Easy","Microsoft",""),
    ("1D DP","House Robber","https://leetcode.com/problems/house-robber/","Medium","Amazon",""),
    ("Circle DP","House Robber II","https://leetcode.com/problems/house-robber-ii/","Medium","Amazon",""),
    ("2D DP","Longest Palindromic Substring","https://leetcode.com/problems/longest-palindromic-substring/","Medium","Amazon",""),
    ("DP Count","Palindromic Substrings","https://leetcode.com/problems/palindromic-substrings/","Medium","Amazon",""),
    ("1D DP","Decode Ways","https://leetcode.com/problems/decode-ways/","Medium","Facebook",""),
    ("Unbounded KS","Coin Change","https://leetcode.com/problems/coin-change/","Medium","Amazon",""),
    ("Kadane Variant","Maximum Product Subarray","https://leetcode.com/problems/maximum-product-subarray/","Medium","Google",""),
    ("1D DP","Word Break","https://leetcode.com/problems/word-break/","Medium","Facebook",""),
    ("LIS","Longest Increasing Subsequence","https://leetcode.com/problems/longest-increasing-subsequence/","Medium","Amazon",""),
    ("0/1 KS","Partition Equal Subset Sum","https://leetcode.com/problems/partition-equal-subset-sum/","Medium","Amazon",""),
    ("Grid DP","Unique Paths","https://leetcode.com/problems/unique-paths/","Medium","Microsoft",""),
    ("LCS","Longest Common Subsequence","https://leetcode.com/problems/longest-common-subsequence/","Medium","Amazon",""),
    ("State Machine","Best Time to Buy and Sell Stock with Cooldown","https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/","Medium","Google",""),
    ("Edit","Edit Distance","https://leetcode.com/problems/edit-distance/","Hard","Google",""),
]
add_sheet("Dynamic Programming", dp_rows)

# 18. Topological Sort (15)
topo_rows = [
    ("Cycle/Topo","Course Schedule","https://leetcode.com/problems/course-schedule/","Medium","Google",""),
    ("Topo Order","Course Schedule II","https://leetcode.com/problems/course-schedule-ii/","Medium","Google",""),
    ("Topo+Graph","Minimum Height Trees","https://leetcode.com/problems/minimum-height-trees/","Medium","—",""),
    ("Topo+Groups","Sort Items by Groups Respecting Dependencies","https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/","Hard","Google",""),
    ("Topo+Recipes","Find All Possible Recipes from Given Supplies","https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/","Medium","Amazon",""),
    ("Topo+Safe","Find Eventual Safe States","https://leetcode.com/problems/find-eventual-safe-states/","Medium","Google",""),
    ("Topo+Check","Sequence Reconstruction","https://leetcode.com/problems/sequence-reconstruction/","Medium","—",""),
    ("Topo+Parallel","Parallel Courses","https://leetcode.com/problems/parallel-courses/","Medium","—",""),
    ("Topo+Parallel","Parallel Courses II","https://leetcode.com/problems/parallel-courses-ii/","Hard","—",""),
    ("Topo+Parallel","Parallel Courses III","https://leetcode.com/problems/parallel-courses-iii/","Hard","—",""),
    ("Topo+Dict","Alien Dictionary (LintCode)","https://www.lintcode.com/problem/892/","Hard","Google",""),
    ("Topo+Reach","Course Schedule IV","https://leetcode.com/problems/course-schedule-iv/","Medium","—",""),
    ("Topo+Build","Minimum Number of Semesters","https://leetcode.com/problems/parallel-courses/","Medium","—",""),
    ("Topo+Prereq","Prerequisite Tasks (GFG)","https://practice.geeksforgeeks.org/problems/prerequisite-tasks/1","Medium","—",""),
    ("Topo+String","Alien Dictionary (GFG)","https://practice.geeksforgeeks.org/problems/alien-dictionary/1","Hard","—",""),
]
add_sheet("Topological Sort", topo_rows)

# 19. Sliding Window DP (15)
swdp_rows = [
    ("Kadane","Maximum Subarray","https://leetcode.com/problems/maximum-subarray/","Medium","Amazon",""),
    ("Window DP","Longest Turbulent Subarray","https://leetcode.com/problems/longest-turbulent-subarray/","Medium","—",""),
    ("Window DP","Longest Subarray of 1's After Deleting One Element","https://leetcode.com/problems/longest-subarray-of-1s-after-deleting-one-element/","Medium","—",""),
    ("Window DP","Max Consecutive Ones II","https://leetcode.com/problems/max-consecutive-ones-ii/","Medium","—",""),
    ("Prefix+Window","Maximum Points You Can Obtain from Cards","https://leetcode.com/problems/maximum-points-you-can-obtain-from-cards/","Medium","Amazon",""),
    ("Window Sum","Grumpy Bookstore Owner","https://leetcode.com/problems/grumpy-bookstore-owner/","Medium","—",""),
    ("Paint House","Paint House","https://leetcode.com/problems/paint-house/","Medium","—",""),
    ("Paint House","Paint House II","https://leetcode.com/problems/paint-house-ii/","Hard","—",""),
    ("DP+Window","Minimum Swaps to Group All 1's Together","https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/","Medium","—",""),
    ("DP+Window","Minimum Swaps to Group All 1's Together II","https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together-ii/","Medium","—",""),
    ("Window Opt","Constrained Subsequence Sum","https://leetcode.com/problems/constrained-subsequence-sum/","Hard","—",""),
    ("Window Opt","Maximum Sum of Two Non-Overlapping Subarrays","https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/","Medium","—",""),
    ("Window Opt","Maximum Sum of 3 Non-Overlapping Subarrays","https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/","Hard","—",""),
    ("Window Opt","Minimum Cost to Make at Least One Valid Path in a Grid","https://leetcode.com/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/","Hard","—",""),
    ("DP+Deque","Jump Game VI","https://leetcode.com/problems/jump-game-vi/","Medium","—",""),
]
add_sheet("Sliding Window DP", swdp_rows)

# 20. String Matching (KMP / RK) (15)
kmp_rows = [
    ("KMP","Find the Index of the First Occurrence in a String","https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/","Medium","Microsoft",""),
    ("KMP","Shortest Palindrome","https://leetcode.com/problems/shortest-palindrome/","Hard","Google",""),
    ("KMP","Repeated Substring Pattern","https://leetcode.com/problems/repeated-substring-pattern/","Easy","—",""),
    ("KMP","Longest Happy Prefix","https://leetcode.com/problems/longest-happy-prefix/","Hard","—",""),
    ("Rabin-Karp","Rolling Hash: Substring with Concatenation of All Words","https://leetcode.com/problems/substring-with-concatenation-of-all-words/","Hard","Google",""),
    ("Z-Algorithm","Z-Function: String Matching Example (GFG)","https://www.geeksforgeeks.org/z-algorithm-linear-time-pattern-searching-algorithm/","Medium","—",""),
    ("Aho-Corasick","Multi-pattern Search (GFG)","https://www.geeksforgeeks.org/aho-corasick-algorithm-pattern-searching/","Hard","—",""),
    ("BK Tree","Approximate Matching (GFG)","https://www.geeksforgeeks.org/bk-tree-introduction-implementation/","Hard","—",""),
    ("Rolling Hash","Longest Duplicate Substring","https://leetcode.com/problems/longest-duplicate-substring/","Hard","Google",""),
    ("Hashing","Group Anagrams","https://leetcode.com/problems/group-anagrams/","Medium","Google",""),
    ("Trie+Match","Implement Trie","https://leetcode.com/problems/implement-trie-prefix-tree/","Medium","Google",""),
    ("Automaton","Regular Expression Matching","https://leetcode.com/problems/regular-expression-matching/","Hard","Google",""),
    ("Automaton","Wildcard Matching","https://leetcode.com/problems/wildcard-matching/","Hard","Google",""),
    ("Suffix","Longest Common Subsequence (as reference)","https://leetcode.com/problems/longest-common-subsequence/","Medium","—",""),
    ("Manacher","Longest Palindromic Substring","https://leetcode.com/problems/longest-palindromic-substring/","Medium","—",""),
]
add_sheet("String Matching (KMP-RK)", kmp_rows)

# 21. Monotonic Stack/Queue (15)
mono_rows = [
    ("Next Greater","Next Greater Element I","https://leetcode.com/problems/next-greater-element-i/","Easy","Amazon",""),
    ("Next Greater","Next Greater Element II","https://leetcode.com/problems/next-greater-element-ii/","Medium","Amazon",""),
    ("Stock","Online Stock Span","https://leetcode.com/problems/online-stock-span/","Medium","Amazon",""),
    ("Temps","Daily Temperatures","https://leetcode.com/problems/daily-temperatures/","Medium","Microsoft",""),
    ("Histogram","Largest Rectangle in Histogram","https://leetcode.com/problems/largest-rectangle-in-histogram/","Hard","Google",""),
    ("Matrix","Maximal Rectangle","https://leetcode.com/problems/maximal-rectangle/","Hard","Facebook",""),
    ("Deque","Sliding Window Maximum","https://leetcode.com/problems/sliding-window-maximum/","Hard","Amazon",""),
    ("Cleanup","Remove Duplicate Letters","https://leetcode.com/problems/remove-duplicate-letters/","Medium","Google",""),
    ("Digits","Remove K Digits","https://leetcode.com/problems/remove-k-digits/","Medium","Google",""),
    ("Collision","Asteroid Collision","https://leetcode.com/problems/asteroid-collision/","Medium","Amazon",""),
    ("Stack Str","Make The String Great","https://leetcode.com/problems/make-the-string-great/","Easy","—",""),
    ("Queue","Constrained Subsequence Sum","https://leetcode.com/problems/constrained-subsequence-sum/","Hard","—",""),
    ("Queue","Longest Continuous Subarray With Absolute Diff <= Limit","https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/","Medium","—",""),
    ("Stack","132 Pattern","https://leetcode.com/problems/132-pattern/","Medium","—",""),
    ("Deque","Sum of Subarray Minimums","https://leetcode.com/problems/sum-of-subarray-minimums/","Medium","—",""),
]
add_sheet("Monotonic Stack-Queue", mono_rows)

# 22. Math + Number Theory (15)
math_rows = [
    ("Primes","Count Primes","https://leetcode.com/problems/count-primes/","Medium","Amazon",""),
    ("Math","Fizz Buzz","https://leetcode.com/problems/fizz-buzz/","Easy","—",""),
    ("Base","Excel Sheet Column Number","https://leetcode.com/problems/excel-sheet-column-number/","Easy","—",""),
    ("Happy","Happy Number","https://leetcode.com/problems/happy-number/","Easy","Google",""),
    ("Reverse","Reverse Integer","https://leetcode.com/problems/reverse-integer/","Medium","—",""),
    ("Roman","Roman to Integer","https://leetcode.com/problems/roman-to-integer/","Easy","—",""),
    ("Roman","Integer to Roman","https://leetcode.com/problems/integer-to-roman/","Medium","—",""),
    ("Power","Pow(x, n)","https://leetcode.com/problems/powx-n/","Medium","Microsoft",""),
    ("Divide","Divide Two Integers","https://leetcode.com/problems/divide-two-integers/","Medium","—",""),
    ("Palindrome","Palindrome Number","https://leetcode.com/problems/palindrome-number/","Easy","—",""),
    ("Fraction","Fraction to Recurring Decimal","https://leetcode.com/problems/fraction-to-recurring-decimal/","Medium","—",""),
    ("Digits","Add Digits","https://leetcode.com/problems/add-digits/","Easy","—",""),
    ("Ugly","Ugly Number","https://leetcode.com/problems/ugly-number/","Easy","—",""),
    ("Ugly","Ugly Number II","https://leetcode.com/problems/ugly-number-ii/","Medium","—",""),
    ("Math","Missing Number","https://leetcode.com/problems/missing-number/","Easy","Google",""),
]
add_sheet("Math + Number Theory", math_rows)

# 23. Segment Tree / Fenwick Tree (15)
seg_rows = [
    ("Fenwick","Range Sum Query - Mutable","https://leetcode.com/problems/range-sum-query-mutable/","Medium","Google",""),
    ("Segment","Range Module","https://leetcode.com/problems/range-module/","Hard","Google",""),
    ("Fenwick","Count of Smaller Numbers After Self","https://leetcode.com/problems/count-of-smaller-numbers-after-self/","Hard","Google",""),
    ("Fenwick","Reverse Pairs","https://leetcode.com/problems/reverse-pairs/","Hard","—",""),
    ("Segment","My Calendar III","https://leetcode.com/problems/my-calendar-iii/","Hard","—",""),
    ("Segment","Falling Squares","https://leetcode.com/problems/falling-squares/","Hard","—",""),
    ("Fenwick","Create Sorted Array through Instructions","https://leetcode.com/problems/create-sorted-array-through-instructions/","Hard","—",""),
    ("Fenwick","Number of Subsequences That Satisfy the Given Sum Condition","https://leetcode.com/problems/number-of-subsequences-that-satisfy-the-given-sum-condition/","Medium","—",""),
    ("Fenwick","Range Sum Query 2D - Mutable (GFG refs)","https://practice.geeksforgeeks.org/problems/range-sum-query-2d/1","Hard","—",""),
    ("Segment","Longest Repeating Character Replacement II (range updates)","https://leetcode.com/","Hard","—",""),
    ("Segment","Count of Range Sum","https://leetcode.com/problems/count-of-range-sum/","Hard","—",""),
    ("Fenwick","Inversion Count (GFG)","https://practice.geeksforgeeks.org/problems/inversion-of-array-1587115620/1","Medium","—",""),
    ("Fenwick","Kth Number Queries (GFG)","https://practice.geeksforgeeks.org/problems/k-th-number/0","Hard","—",""),
    ("Segment","Maximum Sum Queries","https://leetcode.com/problems/maximum-sum-queries/","Hard","—",""),
    ("Fenwick","Shifting Letters II (bit for range)","https://leetcode.com/","Medium","—",""),
]
add_sheet("Segment Tree - Fenwick Tree", seg_rows)

# 24. Game Theory / Minimax (15)
game_rows = [
    ("Nim","Nim Game","https://leetcode.com/problems/nim-game/","Easy","Microsoft",""),
    ("Minimax","Predict the Winner","https://leetcode.com/problems/predict-the-winner/","Medium","Google",""),
    ("DP Game","Stone Game","https://leetcode.com/problems/stone-game/","Medium","Amazon",""),
    ("DP Game","Stone Game II","https://leetcode.com/problems/stone-game-ii/","Medium","Google",""),
    ("DP Game","Stone Game III","https://leetcode.com/problems/stone-game-iii/","Hard","—",""),
    ("DP Game","Stone Game IV","https://leetcode.com/problems/stone-game-iv/","Hard","—",""),
    ("Greedy/GT","Stone Game VI","https://leetcode.com/problems/stone-game-vi/","Medium","—",""),
    ("DP Game","Stone Game VII","https://leetcode.com/problems/stone-game-vii/","Medium","—",""),
    ("DP Game","Can I Win","https://leetcode.com/problems/can-i-win/","Medium","—",""),
    ("DP Game","Flip Game II","https://leetcode.com/problems/flip-game-ii/","Medium","—",""),
    ("DP Cost","Guess Number Higher or Lower II","https://leetcode.com/problems/guess-number-higher-or-lower-ii/","Medium","—",""),
    ("Sprague-Grundy","Divisor Game","https://leetcode.com/problems/divisor-game/","Easy","—",""),
    ("Minimax","Cat and Mouse","https://leetcode.com/problems/cat-and-mouse/","Hard","—",""),
    ("Minimax","Cat and Mouse II","https://leetcode.com/problems/cat-and-mouse-ii/","Hard","—",""),
    ("DP Game","Predict the Winner (variation)","https://leetcode.com/problems/predict-the-winner/","Medium","—",""),
]
add_sheet("Game Theory - Minimax", game_rows)

# 25. Miscellaneous Brainteasers (15)
misc_rows = [
    ("Design","Insert Delete GetRandom O(1)","https://leetcode.com/problems/insert-delete-getrandom-o1/","Medium","Google",""),
    ("Design","Insert Delete GetRandom O(1) - Duplicates allowed","https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/","Hard","—",""),
    ("Random","Linked List Random Node","https://leetcode.com/problems/linked-list-random-node/","Medium","—",""),
    ("Random","Shuffle an Array","https://leetcode.com/problems/shuffle-an-array/","Medium","Amazon",""),
    ("Math/Geo","Max Points on a Line","https://leetcode.com/problems/max-points-on-a-line/","Hard","Google",""),
    ("Design","Encode and Decode TinyURL","https://leetcode.com/problems/encode-and-decode-tinyurl/","Medium","—",""),
    ("Matrix","Spiral Matrix","https://leetcode.com/problems/spiral-matrix/","Medium","Amazon",""),
    ("Matrix","Set Matrix Zeroes","https://leetcode.com/problems/set-matrix-zeroes/","Medium","Amazon",""),
    ("Matrix","Rotate Image","https://leetcode.com/problems/rotate-image/","Medium","Google",""),
    ("Math","Rectangle Area","https://leetcode.com/problems/rectangle-area/","Medium","—",""),
    ("Math","Perfect Squares","https://leetcode.com/problems/perfect-squares/","Medium","—",""),
    ("Design","Design Hit Counter","https://leetcode.com/problems/design-hit-counter/","Medium","—",""),
    ("Greedy","Task Scheduler (dup)","https://leetcode.com/problems/task-scheduler/","Medium","Amazon",""),
    ("Sweep","The Skyline Problem","https://leetcode.com/problems/the-skyline-problem/","Hard","Google",""),
    ("Sweep","Merge Intervals (dup)","https://leetcode.com/problems/merge-intervals/","Medium","Google",""),
]
add_sheet("Misc Brainteasers", misc_rows)

# Save
path = "DSA_25Pattern_375_Tracker.xlsx"
wb.save(path)
path
