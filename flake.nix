{
  description = "SICK build environment";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/release-26.05";

  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem
      (system:
        let
          pkgs = import nixpkgs {
            inherit system;
            config.allowUnfree = true;
          };
        in
        {
          devShells.default = pkgs.mkShell {
            nativeBuildInputs = with pkgs.buildPackages; [
              ncurses
              graalvmPackages.graalvm-ce
              sbt
              scalafmt

              git
              openssl
            ];

          };
        }
      );
}
