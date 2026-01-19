#!/bin/bash
set -e

echo "🔧 Fixing AppArmor profile for Rootless Docker..."

# Create the AppArmor profile
cat <<EOT | sudo tee "/etc/apparmor.d/home.walid.bin.rootlesskit"
# ref: https://ubuntu.com/blog/ubuntu-23-10-restricted-unprivileged-user-namespaces
abi <abi/4.0>,
include <tunables/global>

/home/walid/bin/rootlesskit flags=(unconfined) {
  userns,

  # Site-specific additions and overrides. See local/README for details.
  include if exists <local/home.walid.bin.rootlesskit>
}
EOT

echo "🔄 Restarting AppArmor..."
sudo systemctl restart apparmor.service

echo "🔄 Restarting Docker..."
systemctl --user restart docker

echo "✅ Done! Try running 'docker ps' or 'bash rundb.sh' now."
