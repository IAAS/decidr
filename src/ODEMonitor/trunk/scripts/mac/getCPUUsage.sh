#!/bin/sh
# The DecidR Development Team licenses this file to you under
# the Apache License, Version 2.0 (the "License"); you may
# not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.

# usage getCPUUsage.sh [probe delay [precision]]
#  default probe delay: 1s
#  default precision: 2

# fail on error
set -e
# uncomment for debugging:
#set -x
echo 'ERROR: not yet implemented'
exit 1
# variables
sampleDelay=1
[ -n "${1}" ] && sampleDelay=${1} 
decimals=2
[ -n "${2}" ] && decimals=${2}

uptime1=$(cat /proc/uptime)
sleep ${sampleDelay}
uptime2=$(cat /proc/uptime)

secondsDiff=$(echo "${uptime2%%\ *}-${uptime1%%\ *}" | bc)
idleDiff=$(echo "(${uptime2##*\ }-${uptime1##*\ })*100" | bc)

idlePercent=$($(which echo) -e "scale=${decimals}\n${idleDiff}/${secondsDiff}" | bc)
echo $(echo "100.0-${idlePercent}" | bc)
