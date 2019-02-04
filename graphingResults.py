import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator
from collections import namedtuple


numberVertices = [33,34,85,118,497,592,882,1128,1138,4720,4941,11143,15606, 35588]
accumulatedTimesSlow =(0.120183514,0.041502098,0.186747243,0.26686502,0.765008249,0.910370844,2.220177703,3.6095467169999993,2.7445698420000006,24.355798419,27.578238773,168.822970602,436.701930505, 1788.4635401280004)
accumulatedTimesFast = (0.12124954500000004,0.170452836,0.327119858,0.47107532899999993,0.6343987509999998,0.7777566409999999,1.3552644550000001,1.4722866709999995,1.3164444960000001,2.835436843,3.1689244869999995,6.121770886,10.346227347000001,40.889549222)

numberNetworks = 14
index = np.arange(numberNetworks)
bar_width = 0.45
opacity = 0.7



fig, ax = plt.subplots(figsize=(17, 8))




rects1 = ax.bar(index, accumulatedTimesSlow, bar_width,
                alpha=opacity, color='b',label='Without WSPD')

rects2 = ax.bar(index+bar_width, accumulatedTimesFast, bar_width,
                alpha=opacity, color='g',label='With WSPD')




ax.set_xlabel('Network')
ax.set_ylabel('Accumulated time (s) after 15 iterations - logarithmic scale - s = 1')
ax.set_title('Graph drawing computation time (logarithmic scale)')
ax.set_xticks(index + bar_width / 2)
ax.set_xticklabels(('siggraph_2005', 'karate', 'ash85', 'santa_fe', 'west0497', 'dwt_592', 'qh882', 'facebook', 'jagmesh7', '3elt', 'power_grid_4k', 'fe_4elt2', 'barth5', 'bcsstk31'))
ax.set_yscale('log')
ax.legend()


plt.show()
fig.savefig("networkVisualisation_s=1")