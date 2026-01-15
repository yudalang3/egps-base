#!/bin/bash
# Get script directory (works regardless of where script is called from)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

"${SCRIPT_DIR}/build_jar.sh" /mnt/c/Users/yudal/Documents/project/eGPS2/eGPS_v2.1_windows_x64/dependency-egps/ \
               /mnt/c/Users/yudal/Documents/project/eGPS2/eGPS_v2.1_windows_x64_selfTest/dependency-egps/ \
               /mnt/c/Users/yudal/Documents/project/eGPS2/jars/egps2_collections/egps-main.gui/dependency-egps/ \
               /mnt/c/Users/yudal/Documents/project/eGPS2/jars/egps2_collections/egps-shell/dependency-egps/ \
               /mnt/c/Users/yudal/Documents/project/eGPS2/jars/egps2_collections/egps-pathway.evol.browser/dependency-egps/ \
               /mnt/c/Users/yudal/Documents/project/eGPS2/jars/egps2_collections/egps-TQTools/dependency-egps/ \
               /mnt/c/Users/yudal/Documents/project/eGPS2/jars/egps2_collections/egps-mutationPre/dependency-egps/ \
               /mnt/c/Users/yudal/Documents/project/eGPS2/jars/egps2_collections/egps-sanky-venn/dependency-egps/ \
               /mnt/c/Users/yudal/Documents/project/eGPS2/jars/egps2_collections/egps-heatmap/dependency-egps/ \
               /mnt/c/Users/yudal/Documents/project/eGPS2/jars/egps2_collections/egps-chorddiagram/dependency-egps/
