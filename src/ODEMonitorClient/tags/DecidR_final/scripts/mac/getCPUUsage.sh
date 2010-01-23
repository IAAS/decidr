#!/bin/bash

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


# variables
sampleDelay=1
[ -n "${1}" ] && sampleDelay=${1} 
decimals=2
[ -n "${2}" ] && decimals=${2}

top1=$(top -l 2 -n 1 | tail -n 8 | head -n 3 | grep -i "CPU usage")
sleep ${sampleDelay}
top2=$(top -l 2 -n 1 | tail -n 8 | head -n 3 | grep -i "CPU usage")

top1=${top1%%% idle*}
top2=${top2%%% idle*}
top1=${top1##* }
top2=${top2##* }

echo $(echo -e "scale=${decimals}\n100.0-((${top1}+${top2})/2.0)" | bc)