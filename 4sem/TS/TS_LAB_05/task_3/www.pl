use HTTP::Daemon;
use HTTP::Status;
use IO::File;  

my $d = HTTP::Daemon->new(LocalAddr => 'localhost', LocalPort => 4321,) || die;
print "Please contact me at: <URL:", $d->url, ">\n";


while (my $c = $d->accept) {
    while (my $r = $c->get_request) {
        if ($r->method eq 'GET') {
            $path = $r->uri->path;

            if (index($path, "/wroclaw") == 0) {
                $actual_path = "./my_site/wroclaw";

                if (index($path, "/atrakcje") == 8) {
                    if (index($path, "muzeum_narodowe") != -1) {
                        $actual_path = $actual_path."/atrakcje/muzeum_narodowe.html";
                    }
                    elsif (index($path, "ogrod_japonski") != -1) {
                        $actual_path = $actual_path."/atrakcje/ogrod_japonski.html";
                    }
                    $c->send_file_response($actual_path);
                }
                elsif (length($path) == 8) {
                    $actual_path = $actual_path."/wroclaw.html";
                    $c->send_file_response($actual_path);
                }
                else {
                    $c->send_error(RC_FORBIDDEN);
                }
            }
            elsif (index($path, "/krakow") == 0) {
                $actual_path = "./my_site/krakow";
                
                if (index($path, "/atrakcje") == 7) {
                    if (index($path, "kosciol_mariacki") != -1) {
                        $actual_path = $actual_path."/atrakcje/kosciol_mariacki.html";
                    }
                    elsif (index($path, "smocza_jama") != -1) {
                        $actual_path = $actual_path."/atrakcje/smocza_jama.html";
                    }
                    $c->send_file_response($actual_path);
                }
                elsif (length($path) == 7) {
                    $actual_path = $actual_path."/krakow.html";
                    $c->send_file_response($actual_path);
                }
                else {
                    $c->send_error(RC_FORBIDDEN);
                }
            }
            elsif (length($path) == 1) {
                $c->send_file_response("./my_site/miasta.html");
            }
            else {
                $c->send_error(RC_FORBIDDEN);
            }
        }
        else {
            $c->send_error(RC_FORBIDDEN);
        }
    }
    $c->close;
    undef($c);
}