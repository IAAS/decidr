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
#set -x

# variables
decimals=2
[ -n "${1}" ] && decimals=${1}

# functions
extractUnit ()
{
  echo "${1##*\ }"
}

extractValue ()
{
  value="${1%\ *}"
  value="${value##*\ }"
  echo "${value}"
}

# get contents of /proc/meminfo
memTotal="$(cat /proc/meminfo | grep -i '^MemTotal:')"
swapTotal="$(cat /proc/meminfo | grep -i '^SwapTotal:')"
memFree="$(cat /proc/meminfo | grep -i '^MemFree:')"
swapFree="$(cat /proc/meminfo | grep -i '^SwapFree:')"
memBuffers="$(cat /proc/meminfo | grep -i '^Buffers:')"
memCached="$(cat /proc/meminfo | grep -i '^Cached:')"
swapCached="$(cat /proc/meminfo | grep -i '^SwapCached:')"

# extract units
memTotalUnit=$(extractUnit "${memTotal}")
swapTotalUnit=$(extractUnit "${swapTotal}")
memFreeUnit=$(extractUnit "${memFree}")
swapFreeUnit=$(extractUnit "${swapFree}")
memBuffersUnit=$(extractUnit "${memBuffers}")
memCachedUnit=$(extractUnit "${memCached}")
swapCachedUnit=$(extractUnit "${swapCached}")

#extract values
memTotal="$(extractValue "${memTotal}")"
swapTotal="$(extractValue "${swapTotal}")"
memFree="$(extractValue "${memFree}")"
swapFree="$(extractValue "${swapFree}")"
memBuffers="$(extractValue "${memBuffers}")"
memCached="$(extractValue "${memCached}")"
swapCached="$(extractValue "${swapCached}")"

if [ "${memTotalUnit}" = "${swapTotalUnit}" ] && \
   [ "${memFreeUnit}" = "${swapFreeUnit}" ] && \
   [ "${memCachedUnit}" = "${swapCachedUnit}" ] && \
   [ "${swapTotalUnit}" = "${swapFreeUnit}" ] && \
   [ "${swapCachedUnit}" = "${memBuffersUnit}" ] && \
   [ "${swapFreeUnit}" = "${memBuffersUnit}" ]; then
  memTotal=$((${memTotal}+${swapTotal}))
  memUsed=$((${memTotal}-${memFree}-${swapFree}-${memCached}-${swapCached}-${memBuffers}))
  # percentage used
  memPercent=$($(which echo) -e "scale=${decimals}\n${memUsed}00/${memTotal}" | bc)
else
  echo "ERROR: /proc/meminfo entries have different units"
  exit 1
fi

echo ${memPercent}
