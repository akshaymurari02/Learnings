arr = [1, 3, 5, 7, 9, 11]
n = len(arr)

tree = [0] * (4 * n)
lazy = [0] * (4 * n)    # lazy[i] = pending addition for node i


def build(node, start, end):
    if start == end:
        tree[node] = arr[start]
        return
    mid = (start + end) // 2
    build(2 * node, start, mid)
    build(2 * node + 1, mid + 1, end)
    tree[node] = tree[2 * node] + tree[2 * node + 1]


# Push pending lazy value DOWN to children
def push_down(node, start, end):
    if lazy[node] != 0:
        mid = (start + end) // 2
        left, right = 2 * node, 2 * node + 1

        # apply pending value to children
        tree[left] += lazy[node] * (mid - start + 1)
        tree[right] += lazy[node] * (end - mid)

        # pass the lazy value to children
        lazy[left] += lazy[node]
        lazy[right] += lazy[node]

        # clear current node's lazy
        lazy[node] = 0


# QUERY with lazy propagation
def query(node, start, end, l, r):
    if r < start or end < l:
        return 0

    if l <= start and end <= r:
        return tree[node]

    push_down(node, start, end)   # resolve pending updates first
    mid = (start + end) // 2
    return query(2*node, start, mid, l, r) + query(2*node+1, mid+1, end, l, r)


# POINT UPDATE
def update(node, start, end, idx, val):
    if start == end:
        arr[idx] = val
        tree[node] = val
        return
    push_down(node, start, end)
    mid = (start + end) // 2
    if idx <= mid:
        update(2 * node, start, mid, idx, val)
    else:
        update(2 * node + 1, mid + 1, end, idx, val)
    tree[node] = tree[2 * node] + tree[2 * node + 1]


# RANGE UPDATE — add 'val' to every element in arr[l..r]
def range_update(node, start, end, l, r, val):

    if r < start or end < l:     # no overlap
        return

    if l <= start and end <= r:  # total overlap
        tree[node] += val * (end - start + 1)  # update sum for this range
        lazy[node] += val                       # mark lazy for children
        return

    push_down(node, start, end)  # partial overlap — push down first
    mid = (start + end) // 2
    range_update(2*node, start, mid, l, r, val)
    range_update(2*node+1, mid+1, end, l, r, val)
    tree[node] = tree[2 * node] + tree[2 * node + 1]


# ===== TESTING =====
build(1, 0, n - 1)
print("arr:", arr)                                   # [1, 3, 5, 7, 9, 11]
print("Sum [1..4]:", query(1, 0, n-1, 1, 4))        # 3+5+7+9 = 24

update(1, 0, n-1, 2, 10)                            # arr[2] = 10
print("\nAfter point update arr[2]=10:")
print("Sum [1..4]:", query(1, 0, n-1, 1, 4))        # 3+10+7+9 = 29

range_update(1, 0, n-1, 1, 3, 5)                    # add 5 to arr[1..3]
print("\nAfter range_update [1..3] += 5:")
print("Sum [1..4]:", query(1, 0, n-1, 1, 4))        # 8+15+12+9 = 44
print("Sum [0..5]:", query(1, 0, n-1, 0, n-1))      # 1+8+15+12+9+11 = 56
