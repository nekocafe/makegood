<?php
/* vim: set expandtab tabstop=4 shiftwidth=4: */

/**
 * PHP version 5.3
 *
 * Copyright (c) 2011-2012 KUBO Atsuhiro <kubo@iteman.jp>,
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @package    Stagehand_TestRunner
 * @copyright  2011-2012 KUBO Atsuhiro <kubo@iteman.jp>
 * @license    http://www.opensource.org/licenses/bsd-license.php  New BSD License
 * @version    Release: 3.0.3
 * @since      File available since Release 3.0.0
 */

namespace Stagehand\TestRunner\Process;

use Stagehand\TestRunner\Util\String;

/**
 * @package    Stagehand_TestRunner
 * @copyright  2011-2012 KUBO Atsuhiro <kubo@iteman.jp>
 * @license    http://www.opensource.org/licenses/bsd-license.php  New BSD License
 * @version    Release: 3.0.3
 * @since      Class available since Release 3.0.0
 */
class FatalError
{
    const MESSAGE_PATTERN = "/^((?:Parse|Fatal) error: .+) in (.+?)(?:\((\d+)\) : eval\(\)'d code(?:\(\d+\) : eval\(\)'d code)*)? on line (\d+)$/m";

    /**
     * @var string
     */
    protected $fullMessage;

    /**
     * @var string
     */
    protected $message;

    /**
     * @var string
     */
    protected $file;

    /**
     * @var integer
     */
    protected $line;

    public function __construct($output)
    {
        if (preg_match(self::MESSAGE_PATTERN, ltrim(String::normalizeNewlines($output, String::NEWLINE_UNIX)), $matches)) {
            $this->fullMessage = $matches[0];
            $this->message = $matches[1];
            $this->file = $matches[2];
            if (strlen($matches[3]) > 0) {
                $this->line = (integer) $matches[3];
            } else {
                $this->line = (integer) $matches[4];
            }
        } else {
            $this->fullMessage = $output;
        }
    }

    /**
     * @return string
     */
    public function getFullMessage()
    {
        return $this->fullMessage;
    }

    /**
     * @return string
     */
    public function getMessage()
    {
        return $this->message;
    }

    /**
     * @return string
     */
    public function getFile()
    {
        return $this->file;
    }

    /**
     * @return integer
     */
    public function getLine()
    {
        return $this->line;
    }
}

/*
 * Local Variables:
 * mode: php
 * coding: iso-8859-1
 * tab-width: 4
 * c-basic-offset: 4
 * c-hanging-comment-ender-p: nil
 * indent-tabs-mode: nil
 * End:
 */