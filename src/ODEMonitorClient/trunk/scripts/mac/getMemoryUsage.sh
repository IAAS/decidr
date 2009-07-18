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

# usage getMemoryUsage.sh [precision]
#   default precision: 2

# fail on error
set -e
# uncomment for debugging:
set -x

# variables
decimals=2
[ -n "${1}" ] && decimals=${1}

echo "WARNING: incomplete dummy data"
getTotalMem()
{
  echo "100"
}

getTotalSwap()
{
  echo "100"
}

getFreeMem()
{
  echo "100"
}

getFreeSwap()
{
  echo "100"
}

memTotal=$(getTotalMem)
swapTotal=$(getTotalSwap)
memFree=$(getFreeMem)
swapFree=$(getFreeSwap)

memTotal=$((${memTotal}+${swapTotal}))
memFree=$((${memFree}+${swapFree}))
memUsed=$((${memTotal}-${memFree}))

echo $($(which echo) -e "scale=${decimals}\n${memUsed}00/${memTotal}" | bc)