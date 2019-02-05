import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator
from collections import namedtuple



numberPointClouds = 5
index = np.arange(numberPointClouds)
bar_width = 0.45
opacity = 0.7


"""Closest pair

closestPairSlow =(0,12,62,77,2188)
closestPairFast = (7,36,227,211,845)


fig, ax = plt.subplots()




cpSlow = ax.bar(index, closestPairSlow, bar_width,
                alpha=opacity, color='b',label='Without WSPD')

cpFast = ax.bar(index+bar_width, closestPairFast, bar_width,
                alpha=opacity, color='g',label='With WSPD')




ax.set_xlabel('Point clouds (ordered by number of points)')
ax.set_ylabel('Total computation time (ms) to find the closest pair of points')
# ax.set_title('Graph drawing computation time (logarithmic scale)')
ax.set_xticks(index + bar_width / 2)
ax.set_xticklabels(('Torus_33', 'Nefertiti', 'Bague', 'Triceratops', 'Horse'))
ax.set_yscale('log')
ax.legend()

fig.savefig("/Users/js/Desktop/X/2A/2/INF421/ProjetINF421/PI/results/pointCloudGraphing/closestPair")"""

"""Diameter"""

diameterSlow =(0,10,44,68,2025)
diameterFast = (2,22,403,284,2572)


fig, ax = plt.subplots()




diamSlow = ax.bar(index, diameterSlow, bar_width,
                alpha=opacity, color='b',label='Without WSPD')

diamFast = ax.bar(index+bar_width, diameterFast, bar_width,
                alpha=opacity, color='g',label='With WSPD')




ax.set_xlabel('Point clouds (ordered by number of points)')
ax.set_ylabel('Total computation time (ms) to approximate the diameter')
# ax.set_title('Graph drawing computation time (logarithmic scale)')
ax.set_xticks(index + bar_width / 2)
ax.set_xticklabels(('Torus_33', 'Nefertiti', 'Bague', 'Triceratops', 'Horse'))
ax.set_yscale('log')
ax.legend()

fig.savefig("/Users/js/Desktop/X/2A/2/INF421/ProjetINF421/PI/results/pointCloudGraphing/diameter")