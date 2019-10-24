import math
def bfs(val, goal):
    path = []
    while val != goal:
        for i in range(0, 3):
            if i == 0:
                val = math.sqrt(val)
                path.append(val)
                bfs(val,goal)
            elif i == 1:
                val = math.factorial(val)
                path.append(val)
                bfs(val, goal)
            elif i == 2:
                val = math.floor(val)
                path.append(val)
                bfs(val, goal)
    for temp_V in path:
        print(temp_V + ", ")

bfs(3, 26)

