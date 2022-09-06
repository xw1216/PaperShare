import * as pdf from 'pdfjs-dist'
import {RenderingCancelledException} from 'pdfjs-dist'
import React, {useLayoutEffect, useRef, useState} from "react";
import {RenderTask} from "pdfjs-dist/types/src/display/api";
import {Button, Pagination, Stack} from "@mui/material";
import 'pdfjs-dist/web/pdf_viewer.css';
import {TransformComponent, TransformWrapper} from "@pronestor/react-zoom-pan-pinch";

pdf.GlobalWorkerOptions.workerSrc = require('pdfjs-dist/build/pdf.worker.entry');

export const PDFRender: React.FC<any> = (props) => {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);
    const renderTaskRef = useRef<RenderTask | null>(null);
    const [totalPageNumber, setTotalPageNumber] = useState<number>(1);

    function onPageChange(event: object, page: number) {
        props.setPage(page);
    }

    useLayoutEffect(() => {
        pdf
            .getDocument(props.src)
            .promise
            .then(pdfDocument => {
                setTotalPageNumber(pdfDocument.numPages);
                return pdfDocument.getPage(props.page);
            })
            .then((pdfPage) => {
                const viewport = pdfPage.getViewport({scale: 2});
                const canvas = canvasRef.current;
                if (!canvas) {
                    return Promise.reject();
                }
                canvas.width = viewport.width;
                canvas.height = viewport.height;
                const ctx = canvas.getContext("2d") as CanvasRenderingContext2D;
                renderTaskRef.current?.cancel();
                const renderContext = {
                    canvasContext: ctx,
                    viewport,
                };

                renderTaskRef.current = pdfPage.render(renderContext);
                return renderTaskRef.current.promise;
            })
            .catch(err => {
                if (!(err instanceof RenderingCancelledException)) {
                    console.log(err)
                }
            })
    }, [props.page, props.src]);

    return (
        <div>
            <Stack
                direction={"column"}
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                }}
            >
                <TransformWrapper
                    initialScale={1}
                    centerOnInit={true}
                    doubleClick={{
                        mode: "reset"
                    }}
                    limitToBounds={false}
                >
                    {({zoomIn, zoomOut, resetTransform, ...rest}) => (
                        <React.Fragment>
                            <Stack spacing={1} direction={"row"} my={4}>
                                <Button variant={"contained"} onClick={() => zoomIn()}>放大</Button>
                                <Button variant={"contained"} onClick={() => zoomOut()}>缩小</Button>
                                <Button variant={"outlined"} onClick={() => resetTransform()}>重置</Button>
                            </Stack>
                            <TransformComponent>
                                <canvas ref={canvasRef}/>
                            </TransformComponent>
                        </React.Fragment>
                    )}
                </TransformWrapper>

                <Pagination
                    sx={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                    }}
                    count={totalPageNumber}
                    onChange={onPageChange}
                    size="large"
                />
            </Stack>
        </div>

    )
}