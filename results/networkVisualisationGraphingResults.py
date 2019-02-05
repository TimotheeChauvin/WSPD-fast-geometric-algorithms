import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator
from collections import namedtuple



numberNetworks = 14
index = np.arange(numberNetworks)
bar_width = 0.45
opacity = 0.7

"""s = 1
accumulatedTimesSlow =(
0.120183514,
0.041502098,
0.186747243,
0.26686502,
0.765008249,
0.910370844,
2.220177703,
3.6095467169999993,
2.7445698420000006,
24.355798419,
27.578238773,
168.822970602,
436.701930505,
1788.4635401280004
)
accumulatedTimesFast = (
0.12124954500000004,
0.170452836,
0.327119858,
0.47107532899999993,
0.6343987509999998,
0.7777566409999999,
1.3552644550000001,
1.4722866709999995,
1.3164444960000001,
2.835436843,
3.1689244869999995,
6.121770886,
10.346227347000001,
40.889549222
)
"""


"""s = 0.1
accumulatedTimesSlow =(0.04470437099999999,
0.040689652,
0.10182969000000001,
0.12103356800000001,
0.583067028,
0.62191549,
1.7405559570000004,
1.8549588959999994,
3.4537324550000004,
26.681537544000005,
32.927056349,
195.819184316,
419.387754064,
2086.240292411)
accumulatedTimesFast = (0.045904969,
0.052496204,
0.12143238099999998,
0.11933799599999999,
0.28690576100000004,
0.309235058,
0.47161198699999995,
0.367384414,
0.652864165,
0.9900007009999999,
1.4610589020000002,
2.412228905,
3.0265862389999993,
7.908173548)
"""

s = 0.01
accumulatedTimesSlow =(
0.03526924399999999,
0.050754840999999995,
0.13817261200000003,
0.12284898500000001,
0.813862932,
0.649902977,
1.338157051,
1.868725773,
2.7706574190000004,
32.032298925999996,
30.922507951000007,
152.76314090900001,
436.29286468099997,
2138.8730381650003
)
accumulatedTimesFast =(
0.05755461299999999,
0.089965422,
0.14016171799999996,
0.099831941,
0.247194687,
0.30638802800000003,
0.431177599,
0.38582912099999994,
0.662202424,
1.2266089089999999,
1.2932213380000002,
2.06042434,
3.138568165,
7.126260664999999
)


fig, ax = plt.subplots(figsize=(18, 8))




rects1 = ax.bar(index, accumulatedTimesSlow, bar_width,
                alpha=opacity, color='b',label='Without WSPD')

rects2 = ax.bar(index+bar_width, accumulatedTimesFast, bar_width,
                alpha=opacity, color='g',label='With WSPD')




ax.set_xlabel('\n Networks (ordered by number of vertices)', fontsize = 'larger')
ax.set_ylabel('Total computation time (s) for 15 iterations - logarithmic scale - s = 0,01', fontsize = 'larger')
# ax.set_title('Graph drawing computation time (logarithmic scale)')
ax.set_xticks(index + bar_width / 2)
ax.set_xticklabels(('siggraph_2005', 'karate', 'ash85', 'santa_fe', 'west0497', 'dwt_592', 'qh882', 'facebook', 'jagmesh7', '3elt', 'power_grid_4k', 'fe_4elt2', 'barth5', 'bcsstk31'), fontsize = 'smaller')
ax.set_yscale('log')
ax.set_ylim(top = 10*10*10*10)

ax.legend()

"""s = 1
fig.savefig("/Users/js/Desktop/X/2A/2/INF421/ProjetINF421/PI/results/networkVisualisationGraphing/networkVisualisation_s=0,1")
"""

"""s = 0.1
fig.savefig("/Users/js/Desktop/X/2A/2/INF421/ProjetINF421/PI/results/networkVisualisationGraphing/networkVisualisation_s=0,1")
"""

s = 0.01
fig.savefig("/Users/js/Desktop/X/2A/2/INF421/ProjetINF421/PI/results/networkVisualisationGraphing/networkVisualisation_s=0,01")
